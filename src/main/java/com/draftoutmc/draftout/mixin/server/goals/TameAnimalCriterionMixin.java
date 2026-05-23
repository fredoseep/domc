package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.TameAnimalGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.advancements.criterion.TameAnimalTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TameAnimalTrigger.class})
public class TameAnimalCriterionMixin {
   @Inject(
      method = {"trigger"},
      at = {@At("HEAD")}
   )
   public void onTameAnimal(ServerPlayer player, Animal animal, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal != null && goal instanceof TameAnimalGoal) {
               TameAnimalGoal tameAnimalGoal = (TameAnimalGoal)goal;
               if (!goal.isCompleted() && tameAnimalGoal.getAnimals().contains(animal.getType())) {
                  lockout.completeGoal(tameAnimalGoal, (Player)player);
               }
            }
         }

      }
   }
}
