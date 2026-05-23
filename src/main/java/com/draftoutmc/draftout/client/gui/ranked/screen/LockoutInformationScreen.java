package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class LockoutInformationScreen extends Screen {
   private final Component message;
   private final LinearLayout layout;
   private final Screen screen;
   private final Component buttonMessage;

   public LockoutInformationScreen(String message) {
      this(message, Component.literal("Disconnected"), new TitleScreen(), "Back to title screen");
   }

   public LockoutInformationScreen(String message, Component title) {
      this(message, title, new TitleScreen(), "Back to title screen");
   }

   public LockoutInformationScreen(String message, Component title, Screen nextScreen, String buttonMessage) {
      super(title);
      this.layout = LinearLayout.vertical();
      this.message = Component.literal(message);
      this.screen = nextScreen;
      this.buttonMessage = Component.literal(buttonMessage);
   }

   protected void init() {
      this.layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
      this.layout.addChild(new StringWidget(this.title, this.font));
      this.layout.addChild((new MultiLineTextWidget(this.message, this.font)).setMaxWidth(this.width - 50).setCentered(true));
      this.layout.addChild(Button.builder(this.buttonMessage, (bb) -> Minecraft.getInstance().setScreen(this.screen)).width(200).build());
      this.layout.arrangeElements();
      this.layout.visitWidgets(this::addRenderableWidget);
      this.repositionElements();
   }

   protected void repositionElements() {
      FrameLayout.centerInRectangle(this.layout, this.getRectangle());
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }
}
