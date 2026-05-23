package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainAllTorchesGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainAllTorchesGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain every type of Torch";
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      super.renderTexture(context, x, y, tick);
      Utility.drawStackCount(context, x, y, String.valueOf(ITEMS.size()));
      return true;
   }

   static {
      ITEMS = List.of(Items.TORCH, Items.REDSTONE_TORCH, Items.SOUL_TORCH, Items.COPPER_TORCH);
   }
}
