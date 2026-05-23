package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.ShootFireworkFromCrossbowGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CrossbowItem.class})
public class CrossbowItemMixin {
   @Inject(
      method = {"use"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FFLnet/minecraft/world/entity/LivingEntity;)V"
)}
   )
   public void onShoot(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(name = {"chargedProjectiles"}) ChargedProjectiles chargedProjectiles) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            boolean hasRocket = chargedProjectiles.contains(Items.FIREWORK_ROCKET);
            if (hasRocket) {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof ShootFireworkFromCrossbowGoal) {
                     lockout.completeGoal(goal, player);
                     return;
                  }
               }

            }
         }
      }
   }
}
