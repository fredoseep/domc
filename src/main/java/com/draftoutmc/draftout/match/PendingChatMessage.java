package com.draftoutmc.draftout.match;

import com.draftoutmc.draftout.mixin.server.draftout.ChatComponentAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;

public class PendingChatMessage {
   private final GuiMessage guiMessage;

   private PendingChatMessage(GuiMessage guiMessage) {
      this.guiMessage = guiMessage;
   }

   public static PendingChatMessage add(Component text) {
      Minecraft mc = Minecraft.getInstance();
      ChatComponent chat = mc.gui.getChat();
      GuiMessage msg = new GuiMessage(mc.gui.getGuiTicks(), text, (MessageSignature)null, GuiMessageSource.SYSTEM_CLIENT, GuiMessageTag.systemSinglePlayer());
      Runnable addTask = () -> {
         ((ChatComponentAccessor)chat).invokeAddMessageToDisplayQueue(msg);
         ((ChatComponentAccessor)chat).invokeAddMessageToQueue(msg);
      };
      if (RenderSystem.isOnRenderThread()) {
         addTask.run();
      } else {
         mc.execute(addTask);
      }

      return new PendingChatMessage(msg);
   }

   public void remove() {
      Minecraft mc = Minecraft.getInstance();
      ChatComponent chat = mc.gui.getChat();
      ChatComponentAccessor accessor = (ChatComponentAccessor)chat;
      Runnable removeTask = () -> {
         accessor.getAllMessages().removeIf((m) -> m == this.guiMessage);
         accessor.getTrimmedMessages().removeIf((line) -> line.parent() == this.guiMessage);
         ((ChatComponentAccessor)chat).invokeRefreshTrimmedMessages();
      };
      if (RenderSystem.isOnRenderThread()) {
         removeTask.run();
      } else {
         mc.execute(removeTask);
      }

   }
}
