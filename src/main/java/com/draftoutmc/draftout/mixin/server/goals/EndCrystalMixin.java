package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.obtain.ExplodeEndCrystalGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EndCrystal.class})
public class EndCrystalMixin {
   @Inject(
      method = {"hurtServer"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
)}
   )
   public void onCrystalExplode(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         Entity var7 = source.getEntity();
         if (var7 instanceof Player) {
            Player player = (Player)var7;

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof ExplodeEndCrystalGoal) {
                  lockout.completeGoal(goal, player);
                  return;
               }
            }

         }
      }
   }
}
