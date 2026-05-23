package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.Deal400DamageGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public class LivingEntityMixin {
   @Inject(
      method = {"hurtServer"},
      at = {@At("RETURN")}
   )
   public void onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         Entity var7 = source.getEntity();
         if (var7 instanceof Player) {
            Player player = (Player)var7;
            if ((Boolean)cir.getReturnValue()) {
               if (player.level().isClientSide()) {
                  return;
               }

               if (!lockout.isLockoutPlayer(player.getUUID())) {
                  return;
               }

               LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());
               lockout.damageDealt.putIfAbsent(team, (double)0.0F);
               lockout.damageDealt.merge(team, (double)amount, Double::sum);

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof Deal400DamageGoal) {
                     Deal400DamageGoal deal400DamageGoal = (Deal400DamageGoal)goal;
                     team.sendTooltipUpdate(deal400DamageGoal);
                     if ((Double)lockout.damageDealt.get(team) >= (double)400.0F) {
                        lockout.completeGoal(goal, player);
                     }
                  }
               }

               return;
            }
         }

      }
   }
}
