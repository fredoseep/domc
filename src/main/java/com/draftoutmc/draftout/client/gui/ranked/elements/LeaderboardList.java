package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jspecify.annotations.NonNull;

public class LeaderboardList extends AbstractSelectionList<LeaderboardEntry> {
   public LeaderboardList(Minecraft minecraft, int width, int height, int y, int entryHeight) {
      super(minecraft, width, height, y, entryHeight);
   }

   public int getRowWidth() {
      return 250;
   }

   protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
   }

   public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
      if (this.getEntryAtPosition((double)mouseX, (double)mouseY) != null) {
         graphics.requestCursor(CursorTypes.POINTING_HAND);
      }

   }
}
