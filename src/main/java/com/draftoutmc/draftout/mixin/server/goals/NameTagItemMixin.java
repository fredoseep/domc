package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.NametagMobGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NameTagItem.class})
public class NameTagItemMixin {
   @Inject(
      method = {"interactLivingEntity"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/LivingEntity;setCustomName(Lnet/minecraft/network/chat/Component;)V"
)}
   )
   public void useNameTag(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type, CallbackInfoReturnable<InteractionResult> cir) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            Component customName = (Component)itemStack.get(DataComponents.CUSTOM_NAME);
            if (customName != null) {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof NametagMobGoal) {
                     NametagMobGoal nametagMobGoal = (NametagMobGoal)goal;
                     if (nametagMobGoal.getAcceptableNames().contains(customName.getString()) && nametagMobGoal.getEntity().equals(target.getType())) {
                        lockout.completeGoal(goal, player);
                     }
                  }
               }

            }
         }
      }
   }
}
