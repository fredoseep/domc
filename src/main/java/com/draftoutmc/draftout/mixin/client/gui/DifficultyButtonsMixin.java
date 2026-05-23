package com.draftoutmc.draftout.mixin.client.gui;

import net.minecraft.client.gui.screens.options.DifficultyButtons;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({DifficultyButtons.class})
public class DifficultyButtonsMixin {
   @Redirect(
      method = {"lambda$create$0"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
)
   )
   private static void onSendDifficultyPacket(ClientPacketListener instance, Packet packet) {
      if (packet instanceof ServerboundChangeDifficultyPacket var2) {
         ServerboundChangeDifficultyPacket var10000 = var2;
         Difficulty var6;
         try {
             var6 = var10000.difficulty();
         } catch (Throwable var5) {
            throw new MatchException(var5.toString(), var5);
         }

         if (var6 == Difficulty.PEACEFUL) {
            return;
         }
      }

      instance.send(packet);
   }
}
