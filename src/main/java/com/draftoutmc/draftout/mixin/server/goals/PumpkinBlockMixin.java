package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.MixinContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PumpkinBlock.class})
public class PumpkinBlockMixin {
   @Inject(
      method = {"useItemOn"},
      at = {@At("HEAD")}
   )
   private void captureCarvingPlayer(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
      if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
         if (itemStack.is(Items.SHEARS)) {
            MixinContext.PUMPKIN_CARVING_PLAYER.set(serverPlayer);
         }
      }

   }

   @Inject(
      method = {"useItemOn"},
      at = {@At("RETURN")}
   )
   private void clearCarvingPlayer(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
      MixinContext.PUMPKIN_CARVING_PLAYER.remove();
   }
}
