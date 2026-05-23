package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainWoodenToolsGoal extends ObtainAllItemsGoal {
   public static final List<Item> ITEMS;

   public ObtainWoodenToolsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain all Wooden Tools";
   }

   static {
      ITEMS = List.of(Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_SWORD, Items.WOODEN_SHOVEL, Items.WOODEN_SPEAR);
   }
}
