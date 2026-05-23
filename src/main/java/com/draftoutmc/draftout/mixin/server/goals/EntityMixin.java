package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentCatchesOnFireGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Entity.class})
public class EntityMixin {
   @Inject(
      method = {"setSharedFlagOnFire"},
      at = {@At("HEAD")}
   )
   public void setOnFire(boolean value, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         Entity var5 = (Entity)(Object)this;
         if (var5 instanceof Player) {
            Player player = (Player)var5;
            if (value) {
               if (player.level().isClientSide()) {
                  return;
               }

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof OpponentCatchesOnFireGoal) {
                     lockout.complete1v1Goal(goal, player, false, player.getName().getString() + " caught on fire.");
                  }
               }

               return;
            }
         }

      }
   }
}
