package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.anger.AngerEndermanGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EnderMan.class})
public class EnderManMixin {
   @Inject(
      method = {"setTarget"},
      at = {@At("HEAD")}
   )
   public void m(LivingEntity target, CallbackInfo ci) {
      if (target != null && !target.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            if (target instanceof Player) {
               Player player = (Player)target;

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof AngerEndermanGoal) {
                     lockout.completeGoal(goal, player);
                     return;
                  }
               }

            }
         }
      }
   }
}
