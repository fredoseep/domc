package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.LightCandleGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.phys.BlockHitResult;

public class UseBlockEventHandler implements UseBlockCallback {
   public InteractionResult interact(Player player, Level world, InteractionHand hand, BlockHitResult blockHitResult) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (!Lockout.isLockoutRunning(lockout)) {
         return InteractionResult.PASS;
      } else {
         BlockPos blockPos = blockHitResult.getBlockPos();
         if (!CandleBlock.canLight(world.getBlockState(blockPos))) {
            return InteractionResult.PASS;
         } else {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
               return InteractionResult.PASS;
            } else {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof LightCandleGoal) {
                     lockout.completeGoal(goal, player);
                  }
               }

               return InteractionResult.PASS;
            }
         }
      }
   }
}
