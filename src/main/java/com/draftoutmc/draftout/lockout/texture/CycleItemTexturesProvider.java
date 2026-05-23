package com.draftoutmc.draftout.lockout.texture;

import com.draftoutmc.draftout.LockoutConfig;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.Item;

public interface CycleItemTexturesProvider extends CustomTextureRenderer {
   List<Item> getItemsToDisplay();

   default boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      int cycleTicks = LockoutConfig.getInstance().cycleSpeed / 50;
      int mod = tick % (cycleTicks * this.getItemsToDisplay().size());
      context.item(((Item)this.getItemsToDisplay().get(mod / cycleTicks)).getDefaultInstance(), x, y);
      return true;
   }
}
