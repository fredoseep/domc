package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jspecify.annotations.NonNull;

public class MatchList extends AbstractSelectionList<MatchListEntry> {
   public MatchList(Minecraft minecraft, int width, int height, int y, int entryHeight) {
      super(minecraft, width, height, y, entryHeight);
   }

   public int getRowWidth() {
      return 150;
   }

   protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
   }

   protected void extractSelection(GuiGraphicsExtractor graphics, MatchListEntry entry, int outlineColor) {
      int outlineX0 = entry.getX() - 10;
      int outlineY0 = entry.getY();
      int outlineX1 = outlineX0 + entry.getWidth() + 20;
      int outlineY1 = outlineY0 + entry.getHeight();
      graphics.fill(outlineX0, outlineY0, outlineX1, outlineY1, outlineColor);
      graphics.fill(outlineX0 + 1, outlineY0 + 1, outlineX1 - 1, outlineY1 - 1, -16777216);
   }

   public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
      if (this.getEntryAtPosition((double)mouseX, (double)mouseY) != null) {
         graphics.requestCursor(CursorTypes.POINTING_HAND);
      }

   }
}
