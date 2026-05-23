package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainStoneToolsGoal extends ObtainAllItemsGoal {
   public static final List<Item> ITEMS;

   public ObtainStoneToolsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain all Stone Tools";
   }

   static {
      ITEMS = List.of(Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_SPEAR);
   }
}
