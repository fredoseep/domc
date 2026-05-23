package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainAllPumpkinsGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainAllPumpkinsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain every type of Pumpkin";
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      super.renderTexture(context, x, y, tick);
      Utility.drawStackCount(context, x, y, String.valueOf(ITEMS.size()));
      return true;
   }

   static {
      ITEMS = List.of(Items.PUMPKIN, Items.CARVED_PUMPKIN, Items.JACK_O_LANTERN);
   }
}
