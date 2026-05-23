package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.UseBrushOnSuspiciousBlock;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BrushableBlockEntity.class})
public class BrushableBlockEntityMixin {
   @Inject(
      method = {"brushingCompleted"},
      at = {@At("HEAD")}
   )
   public void finishBrushing(ServerLevel level, LivingEntity user, ItemStack brush, CallbackInfo ci) {
      if (!level.isClientSide()) {
         if (user instanceof Player) {
            Player player = (Player)user;
            Lockout lockout = LockoutMatchData.getLockout();
            if (!Lockout.isLockoutRunning(lockout)) {
               return;
            }

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof UseBrushOnSuspiciousBlock) {
                  lockout.completeGoal(goal, player);
               }
            }
         }

      }
   }
}
