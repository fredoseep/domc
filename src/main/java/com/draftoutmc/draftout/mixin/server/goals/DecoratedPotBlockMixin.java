package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.FillDecoratedPotGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DecoratedPotBlock.class})
public class DecoratedPotBlockMixin {
   @Inject(
      method = {"useItemOn"},
      at = {@At("RETURN")}
   )
   public void onUse(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
      if (!level.isClientSide() && cir.getReturnValue() == InteractionResult.SUCCESS) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            DecoratedPotBlockEntity decoratedPotBlockEntity = (DecoratedPotBlockEntity)level.getBlockEntity(pos);
            if (decoratedPotBlockEntity != null) {
               ItemStack item = decoratedPotBlockEntity.getTheItem();
               if (item.count() >= item.getMaxStackSize()) {
                  for(Goal goal : lockout.getBoard().getGoals()) {
                     if (goal != null && !goal.isCompleted() && goal instanceof FillDecoratedPotGoal) {
                        lockout.completeGoal(goal, player);
                        return;
                     }
                  }

               }
            }
         }
      }
   }
}
