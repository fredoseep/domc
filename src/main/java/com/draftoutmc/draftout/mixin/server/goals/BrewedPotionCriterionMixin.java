package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.ObtainPotionItemGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.advancements.criterion.BrewedPotionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BrewedPotionTrigger.class})
public class BrewedPotionCriterionMixin {
   @Inject(
      method = {"trigger"},
      at = {@At("HEAD")}
   )
   public void onTrigger(ServerPlayer player, Holder<Potion> potion, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal != null && !goal.isCompleted() && goal instanceof ObtainPotionItemGoal) {
               ObtainPotionItemGoal obtainPotionItemGoal = (ObtainPotionItemGoal)goal;
               if (obtainPotionItemGoal.getPotions().contains(potion)) {
                  lockout.completeGoal(obtainPotionItemGoal, (Player)player);
               }
            }
         }

      }
   }
}
