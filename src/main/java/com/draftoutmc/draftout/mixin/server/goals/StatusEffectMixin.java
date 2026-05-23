package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.status_effect.GetXStatusEffectsGoal;
import com.draftoutmc.draftout.lockout.interfaces.StatusEffectGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MobEffect.class})
public class StatusEffectMixin {
   @Inject(
      method = {"onEffectStarted"},
      at = {@At("HEAD")}
   )
   public void onApplied(LivingEntity entity, int amplifier, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (entity instanceof Player) {
            Player player = (Player)entity;
            if (!player.level().isClientSide()) {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted()) {
                     MobEffect statusEffect = (MobEffect)(Object)this;
                     if (goal instanceof StatusEffectGoal) {
                        StatusEffectGoal statusEffectGoal = (StatusEffectGoal)goal;
                        if (statusEffectGoal.getStatusEffect().equals(statusEffect)) {
                           lockout.completeGoal(statusEffectGoal, (Player)player);
                        }
                     }

                     if (goal instanceof GetXStatusEffectsGoal) {
                        GetXStatusEffectsGoal getXStatusEffectsGoal = (GetXStatusEffectsGoal)goal;
                        if (player.getActiveEffects().size() >= getXStatusEffectsGoal.getAmount()) {
                           lockout.completeGoal(goal, player);
                        }
                     }
                  }
               }

            }
         }
      }
   }
}
