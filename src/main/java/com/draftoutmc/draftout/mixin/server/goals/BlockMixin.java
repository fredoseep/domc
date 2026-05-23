package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.MineBlockGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Block.class})
public class BlockMixin {
   @Inject(
      method = {"playerWillDestroy"},
      at = {@At("HEAD")}
   )
   public void onBreak(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && goal instanceof MineBlockGoal) {
                  MineBlockGoal mineBlockGoal = (MineBlockGoal)goal;
                  if (!goal.isCompleted() && mineBlockGoal.getItems().contains(state.getBlock().asItem())) {
                     lockout.completeGoal(goal, player);
                  }
               }
            }

         }
      }
   }
}
