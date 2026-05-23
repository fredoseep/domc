package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainIronToolsGoal extends ObtainAllItemsGoal {
   public static final List<Item> ITEMS;

   public ObtainIronToolsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain all Iron Tools";
   }

   static {
      ITEMS = List.of(Items.IRON_AXE, Items.IRON_HOE, Items.IRON_PICKAXE, Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_SPEAR);
   }
}
