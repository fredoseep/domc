package com.draftoutmc.draftout.client.gui.ranked.elements;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public class ItemGridClientTooltipComponent implements ClientTooltipComponent {
   private static final int SLOT_SIZE = 18;
   private static final int PADDING = 2;
   private final List<ItemStack> items;
   private final int columns;

   public ItemGridClientTooltipComponent(ItemGridTooltip data) {
      this.items = data.items();
      this.columns = data.columns();
   }

   private int rows() {
      return this.items.isEmpty() ? 0 : (int)Math.ceil((double)this.items.size() / (double)this.columns);
   }

   private int cols() {
      return Math.min(this.items.size(), this.columns);
   }

   public int getHeight(Font font) {
      return this.rows() * 18 + 4;
   }

   public int getWidth(Font font) {
      return this.cols() * 18 + 4;
   }

   public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
      for(int i = 0; i < this.items.size(); ++i) {
         int col = i % this.columns;
         int row = i / this.columns;
         int sx = x + 2 + col * 18;
         int sy = y + 2 + row * 18;
         graphics.fill(sx, sy, sx + 16, sy + 16, Integer.MIN_VALUE);
         graphics.outline(sx, sy, 16, 16, 1090519039);
      }

   }

   public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
      for(int i = 0; i < this.items.size(); ++i) {
         int col = i % this.columns;
         int row = i / this.columns;
         int ix = x + 2 + col * 18;
         int iy = y + 2 + row * 18;
         graphics.item((ItemStack)this.items.get(i), ix, iy);
         graphics.itemDecorations(font, (ItemStack)this.items.get(i), ix, iy);
      }

   }
}
