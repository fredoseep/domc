package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.FillChiseledBookshelfGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ChiseledBookShelfBlock.class})
public class ChiseledBookshelfBlockMixin {
   @Inject(
      method = {"useItemOn"},
      at = {@At("RETURN")}
   )
   public void onUseWithItem(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            ChiseledBookShelfBlockEntity blockEntity = (ChiseledBookShelfBlockEntity)level.getBlockEntity(pos);
            if (blockEntity != null) {
               if (cir.getReturnValue() == InteractionResult.SUCCESS && blockEntity.count() >= 6) {
                  for(Goal goal : lockout.getBoard().getGoals()) {
                     if (goal != null && !goal.isCompleted() && goal instanceof FillChiseledBookshelfGoal) {
                        lockout.completeGoal(goal, player);
                     }
                  }

               }
            }
         }
      }
   }
}
