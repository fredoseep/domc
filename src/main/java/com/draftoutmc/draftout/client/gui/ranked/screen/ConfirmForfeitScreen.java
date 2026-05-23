package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.client.gui.ranked.elements.CountdownButton;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class ConfirmForfeitScreen extends Screen {
   public static final long forfeitTime = 60000L;
   public static final Function<Lockout, Boolean> canForfeit = (lockout) -> System.currentTimeMillis() - lockout.getStartTimeMillis() > 60000L;
   private static final Component title = Component.empty();
   private static final Component message;

   public ConfirmForfeitScreen() {
      super(title);
   }

   protected void init() {
      int buttonWidth = 100;
      int buttonY = this.height / 2 + 20;
      Lockout lockout = LockoutMatchData.getLockout();
      this.addRenderableWidget(CountdownButton.fromPlain((Button.Plain)Button.builder(Component.literal("Yes"), (bb) -> {
         if (LockoutMatchData.isInMatch() && (Boolean)canForfeit.apply(lockout)) {
            ServerConnection.getInstance().sendForfeit();
            Minecraft.getInstance().setScreen((Screen)null);
         }

      }).bounds(this.width / 2 - buttonWidth - 5, buttonY, buttonWidth, 20).build(), 3000L));
      this.addRenderableWidget(Button.builder(Component.literal("No"), (bb) -> Minecraft.getInstance().setScreen((Screen)null)).bounds(this.width / 2 + 5, buttonY, buttonWidth, 20).build());
   }

   public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.centeredText(this.font, message, this.width / 2, this.height / 2 - 10, -1);
   }

   static {
      message = Component.literal("Are you sure you want to forfeit?").withStyle(ChatFormatting.WHITE);
   }
}
