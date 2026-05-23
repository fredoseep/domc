package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.workstation.UseLoomGoal;
import com.draftoutmc.draftout.lockout.goals.workstation.UseStonecutterGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Slot.class})
public abstract class SlotMixin {
   @Inject(
      method = {"onTake"},
      at = {@At("HEAD")}
   )
   public void onTakeItem(Player player, ItemStack stack, CallbackInfo ci) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted()) {
                  if (goal instanceof UseStonecutterGoal) {
                     AbstractContainerMenu var8 = player.containerMenu;
                     if (var8 instanceof StonecutterMenu) {
                        StonecutterMenu stonecutterScreenHandler = (StonecutterMenu)var8;
                        Slot currentSlot = (Slot)(Object)this;
                        if (currentSlot == stonecutterScreenHandler.slots.get(1)) {
                           lockout.completeGoal(goal, player);
                        }
                     }
                  }

                  if (goal instanceof UseLoomGoal) {
                     AbstractContainerMenu var10 = player.containerMenu;
                     if (var10 instanceof LoomMenu) {
                        LoomMenu loomScreenHandler = (LoomMenu)var10;
                        Slot currentSlot = (Slot)(Object)this;
                        if (currentSlot == loomScreenHandler.getResultSlot()) {
                           lockout.completeGoal(goal, player);
                        }
                     }
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"setByPlayer(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V"},
      at = {@At("TAIL")}
   )
   private void onSet(ItemStack itemStack, ItemStack previous, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         ;
      }
   }
}
