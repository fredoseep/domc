package com.draftoutmc.draftout.mixin.server.draftout;

import java.util.List;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ChatComponent.class})
public interface ChatComponentAccessor {
   @Accessor("allMessages")
   List<GuiMessage> getAllMessages();

   @Accessor("trimmedMessages")
   List<GuiMessage.Line> getTrimmedMessages();

   @Invoker("addMessageToDisplayQueue")
   void invokeAddMessageToDisplayQueue(GuiMessage var1);

   @Invoker("addMessageToQueue")
   void invokeAddMessageToQueue(GuiMessage var1);

   @Invoker("refreshTrimmedMessages")
   void invokeRefreshTrimmedMessages();
}
