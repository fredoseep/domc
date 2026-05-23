package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.UseGlowInkSignGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.GlowInkSacItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GlowInkSacItem.class})
public class GlowInkSacItemMixin {
   @Inject(
      method = {"tryApplyToSign"},
      at = {@At("RETURN")}
   )
   public void useOnSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player, CallbackInfoReturnable<Boolean> cir) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            if ((Boolean)cir.getReturnValue()) {
               Item signItem = level.getBlockState(sign.getBlockPos()).getBlock().asItem();

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof UseGlowInkSignGoal) {
                     UseGlowInkSignGoal useGlowInkSignGoal = (UseGlowInkSignGoal)goal;
                     if (useGlowInkSignGoal.getSignItems().contains(signItem)) {
                        lockout.completeGoal(goal, player);
                     }
                  }
               }

            }
         }
      }
   }
}
