package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainDiamondToolsGoal extends ObtainAllItemsGoal {
   public static final List<Item> ITEMS;

   public ObtainDiamondToolsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain all Diamond Tools";
   }

   static {
      ITEMS = List.of(Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.DIAMOND_SPEAR);
   }
}
