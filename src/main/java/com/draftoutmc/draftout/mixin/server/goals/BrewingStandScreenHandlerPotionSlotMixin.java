package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.brewing.BrewLingeringPotionGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BrewingStandMenu.PotionSlot.class})
public class BrewingStandScreenHandlerPotionSlotMixin {
   @Inject(
      method = {"onTake"},
      at = {@At("TAIL")}
   )
   public void onTakeItem(Player player, ItemStack carried, CallbackInfo ci) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && carried.getItem() instanceof LingeringPotionItem && goal instanceof BrewLingeringPotionGoal) {
                  lockout.completeGoal(goal, player);
               }
            }

         }
      }
   }
}
