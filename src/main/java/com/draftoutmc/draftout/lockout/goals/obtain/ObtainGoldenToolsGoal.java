package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainGoldenToolsGoal extends ObtainAllItemsGoal {
   public static final List<Item> ITEMS;

   public ObtainGoldenToolsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain all Golden Tools";
   }

   static {
      ITEMS = List.of(Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.GOLDEN_SPEAR);
   }
}
