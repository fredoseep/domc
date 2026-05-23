package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.ItemFrameInItemFrameGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemFrame.class})
public class ItemFrameEntityMixin {
   @Inject(
      method = {"interact"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setItem(Lnet/minecraft/world/item/ItemStack;)V"
)}
   )
   public void onAddItem(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
      if (!player.level().isClientSide()) {
         ItemFrame itemFrame = (ItemFrame)(Object)this;
         if (!(itemFrame instanceof GlowItemFrame)) {
            ItemStack value = player.getItemInHand(hand);
            if (!value.isEmpty() && value.getItem().equals(Items.ITEM_FRAME)) {
               Lockout lockout = LockoutMatchData.getLockout();
               if (Lockout.isLockoutRunning(lockout)) {
                  for(Goal goal : lockout.getBoard().getGoals()) {
                     if (goal != null && !goal.isCompleted() && goal instanceof ItemFrameInItemFrameGoal) {
                        lockout.completeGoal(goal, player);
                     }
                  }

               }
            }
         }
      }
   }
}
