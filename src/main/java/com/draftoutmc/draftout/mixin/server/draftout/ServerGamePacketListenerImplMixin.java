package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.TickThrottler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ServerGamePacketListenerImpl.class})
public class ServerGamePacketListenerImplMixin {
   @Final
   @Shadow
   private TickThrottler chatSpamThrottler;

   @Redirect(
      method = {"broadcastChatMessage"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V"
)
   )
   public void dontAllowSpam(PlayerList instance, PlayerChatMessage message, ServerPlayer sender, ChatType.Bound chatType) {
      if (LockoutMatchData.isInMatchWorld()) {
         if (!this.chatSpamThrottler.isUnderThreshold()) {
            sender.sendSystemMessage(Component.literal("Please do not spam!").withStyle(new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.RED}));
         } else {
            ServerConnection.getInstance().sendChatMessage(message.decoratedContent().getString());
            instance.broadcastChatMessage(message, sender, chatType);
         }
      } else {
         instance.broadcastChatMessage(message, sender, chatType);
      }

   }

   @Redirect(
      method = {"detectRateSpam"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V"
)
   )
   public void dontKickForSpam(ServerGamePacketListenerImpl instance, Component component) {
      if (!LockoutMatchData.isInMatchWorld()) {
         instance.disconnect(component);
      }

   }
}
