package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.MixinContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockItem.class})
public class BlockItemMixin {
   @Inject(
      method = {"place"},
      at = {@At("HEAD")}
   )
   private void capturePlayer(BlockPlaceContext placeContext, CallbackInfoReturnable<InteractionResult> cir) {
      Player var4 = placeContext.getPlayer();
      if (var4 instanceof ServerPlayer player) {
         MixinContext.BLOCK_PLACING_PLAYER.set(player);
      }

   }

   @Inject(
      method = {"place"},
      at = {@At("RETURN")}
   )
   private void clearPlayer(BlockPlaceContext placeContext, CallbackInfoReturnable<InteractionResult> cir) {
      MixinContext.BLOCK_PLACING_PLAYER.remove();
   }
}
