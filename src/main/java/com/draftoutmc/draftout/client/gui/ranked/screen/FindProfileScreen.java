package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class FindProfileScreen extends Screen {
   private EditBox input;

   protected FindProfileScreen() {
      super(Component.empty());
   }

   protected void init() {
      super.init();
      int width = 120;
      int height = 20;
      int gap = 4;
      this.addRenderableWidget(Button.builder(Component.literal("Back to Main"), (bb) -> Minecraft.getInstance().setScreen(new LockoutMainScreen())).bounds(8, this.height - height - 8, 100, height).build());
      this.addRenderableWidget(Button.builder(Component.literal("Search Profile"), (bb) -> this.trySearch()).bounds(this.width / 2 - width / 2, this.height / 2 + gap / 2, width, height).build());
      int var10004 = this.width / 2 - width / 2;
      int var10005 = this.height / 2 - height - gap / 2;
      this.input = new EditBox(Minecraft.getInstance().font, var10004, var10005, width, height, (EditBox)null, Component.empty());
      this.input.setMaxLength(16);
      this.input.setVisible(true);
      this.setInitialFocus(this.input);
      this.addRenderableWidget(this.input);
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.centeredText(this.font, "Profile Search", this.width / 2, 8, -1);
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() != 257 && event.key() != 335) {
         return this.input.keyPressed(event) ? true : super.keyPressed(event);
      } else {
         this.trySearch();
         return true;
      }
   }

   private void trySearch() {
      if (!this.input.getValue().trim().isEmpty()) {
         Minecraft mc = Minecraft.getInstance();
         ServerConnection.getInstance().setPreviousScreen();
         ServerConnection.getInstance().sendProfileRequest(this.input.getValue());
         mc.setScreen(new LockoutWaitingScreen("Loading profile..."));
      }

   }
}
