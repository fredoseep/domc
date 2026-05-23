package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.workstation.UseGrindstoneGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.world.inventory.GrindstoneMenu$4"}
)
public class GrindstoneScreenHandlerOutputSlotMixin {
   @Inject(
      method = {"onTake"},
      at = {@At("HEAD")}
   )
   public void onTakeItem(Player player, ItemStack carried, CallbackInfo ci) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof UseGrindstoneGoal) {
                  AbstractContainerMenu var8 = player.containerMenu;
                  if (var8 instanceof GrindstoneMenu) {
                     GrindstoneMenu grindstoneScreenHandler = (GrindstoneMenu)var8;
                     Slot currentSlot = (Slot) (Object) this;

                     // 使用转换后的 currentSlot 进行实例比对
                     if (currentSlot == grindstoneScreenHandler.slots.get(2)) {
                        lockout.completeGoal(goal, player);
                     }
                  }
               }
            }

         }
      }
   }
}
