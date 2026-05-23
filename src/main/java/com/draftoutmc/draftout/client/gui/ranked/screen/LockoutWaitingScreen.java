package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.GenericWaitingScreen;
import net.minecraft.network.chat.Component;

public class LockoutWaitingScreen extends GenericWaitingScreen {
   public LockoutWaitingScreen(String message) {
      super(Component.literal("Loading..."), true, Component.literal(message), Component.empty(), () -> {
      }, 0, false, false);
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }
}
