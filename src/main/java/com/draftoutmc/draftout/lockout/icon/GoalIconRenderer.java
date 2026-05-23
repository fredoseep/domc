package com.draftoutmc.draftout.lockout.icon;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@FunctionalInterface
public interface GoalIconRenderer {
   void render(GuiGraphicsExtractor var1, Font var2, int var3, int var4);
}
