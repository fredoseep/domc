package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.status_effect.RemoveStatusEffectUsingMilkGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ConsumeItemTrigger.class})
public class ConsumeItemCriterionMixin {
   @Inject(
      method = {"trigger"},
      at = {@At("HEAD")}
   )
   public void trigger(ServerPlayer player, ItemStack itemStack, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal != null && !goal.isCompleted() && goal instanceof RemoveStatusEffectUsingMilkGoal && !player.getActiveEffects().isEmpty() && itemStack.getItem().equals(Items.MILK_BUCKET)) {
               lockout.completeGoal(goal, (Player)player);
               return;
            }
         }

      }
   }
}
