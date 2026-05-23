package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.BreakItemGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemStack.class})
public class ItemStackMixin {
   @Inject(
      method = {"applyDamage"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
)}
   )
   public void onBreak(int newDamage, @Nullable ServerPlayer player, Consumer<Item> onBreak, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (!player.level().isClientSide()) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof BreakItemGoal) {
                  BreakItemGoal breakItemGoal = (BreakItemGoal)goal;
                  if (breakItemGoal.getItems().contains(((ItemStack)(Object)this).getItem())) {
                     lockout.completeGoal(goal, (Player)player);
                  }
               }
            }

         }
      }
   }
}
