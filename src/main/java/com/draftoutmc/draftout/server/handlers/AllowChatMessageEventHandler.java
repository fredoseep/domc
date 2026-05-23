package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;

public class AllowChatMessageEventHandler implements ServerMessageEvents.AllowChatMessage {
   public boolean allowChatMessage(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound parameters) {
      if (LockoutMatchData.isInMatch()) {
         ServerConnection.getInstance().sendChatMessage(message.decoratedContent().getString());
      }

      sender.sendSystemMessage(Component.literal("<" + sender.getName().getString() + "> ").append(message.decoratedContent().getString()));
      return false;
   }
}
