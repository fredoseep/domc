package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.wear_armor.PutWolfArmorOnWolfGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Wolf.class})
public class WolfEntityMixin {
   @Inject(
      method = {"mobInteract"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;setBodyArmorItem(Lnet/minecraft/world/item/ItemStack;)V",
   ordinal = 0
)}
   )
   public void onEquipArmor(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof PutWolfArmorOnWolfGoal) {
                  lockout.completeGoal(goal, player);
                  return;
               }
            }

         }
      }
   }
}
