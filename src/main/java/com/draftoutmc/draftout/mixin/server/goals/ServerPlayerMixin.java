package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.interfaces.SpyOnMobGoal;
import com.draftoutmc.draftout.lockout.interfaces.SpyOnUniqueMobsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.LinkedHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayer.class})
public class ServerPlayerMixin {
   @Inject(
      method = {"updateUsingItem"},
      at = {@At("HEAD")}
   )
   private void onUpdateUsingItem(ItemStack useItem, CallbackInfo ci) {
      if (useItem.is(Items.SPYGLASS)) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            ServerPlayer player = (ServerPlayer)(Object)this;
            if (lockout.isLockoutPlayer(player.getUUID())) {
               LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());
               Vec3 from = player.getEyePosition();
               Vec3 viewVec = player.getViewVector(1.0F);
               Vec3 to = from.add(viewVec.x * (double)100.0F, viewVec.y * (double)100.0F, viewVec.z * (double)100.0F);
               EntityHitResult hit = ProjectileUtil.getEntityHitResult(player.level(), player, from, to, (new AABB(from, to)).inflate((double)1.0F), (e) -> !e.isSpectator(), 0.0F);
               if (hit != null && hit.getType() == Type.ENTITY) {
                  Entity entity = hit.getEntity();
                  if (player.hasLineOfSight(entity)) {
                     if (entity instanceof Mob) {
                        Mob mob = (Mob)entity;
                        boolean addedMob = false;
                        int size = 0;

                        for(Goal goal : lockout.getBoard().getGoals()) {
                           if (goal != null && !goal.isCompleted()) {
                              if (goal instanceof SpyOnUniqueMobsGoal) {
                                 SpyOnUniqueMobsGoal spyOnUniqueMobsGoal = (SpyOnUniqueMobsGoal)goal;
                                 spyOnUniqueMobsGoal.getTrackerMap().putIfAbsent(team, new LinkedHashSet());
                                 addedMob |= ((LinkedHashSet)spyOnUniqueMobsGoal.getTrackerMap().get(team)).add(mob.getType());
                                 size = ((LinkedHashSet)spyOnUniqueMobsGoal.getTrackerMap().get(team)).size();
                                 team.sendTooltipUpdate((Goal&HasTooltipInfo)goal);
                                 if (size >= spyOnUniqueMobsGoal.getAmount()) {
                                    lockout.completeGoal(spyOnUniqueMobsGoal, (LockoutTeam)team);
                                 }
                              }

                              if (goal instanceof SpyOnMobGoal) {
                                 SpyOnMobGoal spyOnMobGoal = (SpyOnMobGoal)goal;
                                 if (mob.getType().equals(spyOnMobGoal.getMob())) {
                                    lockout.completeGoal(spyOnMobGoal, (LockoutTeam)team);
                                 }
                              }
                           }
                        }

                        if (addedMob) {
                           player.level().playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.MASTER, 2.0F, 1.0F);
                           if (size % 5 == 0) {
                              String var10001 = String.valueOf(ChatFormatting.GRAY);
                              player.sendSystemMessage(Component.nullToEmpty(var10001 + String.valueOf(ChatFormatting.ITALIC) + "You have spied on " + size + " unique mobs."));
                           }

                           player.sendOverlayMessage(Component.nullToEmpty("Mobs spied on: " + size));
                        }

                     }
                  }
               }
            }
         }
      }
   }
}
