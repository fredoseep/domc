package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;

public class ConnectTwitchScreen extends Screen {
   private static final Component title = Component.empty();
   private static final Component message;
   private static final Component note;
   private final String url;

   public ConnectTwitchScreen(String url) {
      super(title);
      this.url = url;
   }

   protected void init() {
      int buttonWidth = 200;
      int buttonHeight = 20;
      int gap = 4;
      int buttonY = this.height / 2 + 20;
      this.addRenderableWidget(Button.builder(Component.literal("Open in browser"), (bb) -> Util.getPlatform().openUri(this.url)).bounds(this.width / 2 - buttonWidth / 2, buttonY, buttonWidth, buttonHeight).build());
      this.addRenderableWidget(Button.builder(Component.literal("Copy URL to Clipboard"), (bb) -> this.minecraft.keyboardHandler.setClipboard(this.url)).bounds(this.width / 2 - buttonWidth / 2, buttonY + buttonHeight + gap, buttonWidth, buttonHeight).build());
      this.addRenderableWidget(Button.builder(Component.literal("Back to Main Menu"), (bb) -> Minecraft.getInstance().setScreen(new LockoutMainScreen())).bounds(this.width / 2 - buttonWidth / 2, buttonY + buttonHeight * 2 + gap * 2, buttonWidth, buttonHeight).build());
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.centeredText(this.font, message, this.width / 2, this.height / 2 - 30, -1);
      graphics.centeredText(this.font, note, this.width / 2, this.height / 2 - 18, -1);
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   static {
      message = Component.literal("Connect your Twitch below").withColor(ARGB.color(255, 9520895)).withStyle(ChatFormatting.BOLD);
      note = Component.literal("NOTE: Your e-mail doesn't get stored.").withStyle(ChatFormatting.GRAY);
   }
}
