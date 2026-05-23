package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.workstation.UseSmithingTableGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SmithingMenu.class})
public class SmithingScreenHandlerMixin {
   @Inject(
      method = {"onTake"},
      at = {@At("TAIL")}
   )
   public void onTakeOutputMixin(Player player, ItemStack stack, CallbackInfo ci) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof UseSmithingTableGoal) {
                  lockout.completeGoal(goal, player);
               }
            }

         }
      }
   }
}
