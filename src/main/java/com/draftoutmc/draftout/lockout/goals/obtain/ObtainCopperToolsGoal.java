package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainCopperToolsGoal extends ObtainAllItemsGoal {
   public static final List<Item> ITEMS;

   public ObtainCopperToolsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain all Copper Tools";
   }

   static {
      ITEMS = List.of(Items.COPPER_AXE, Items.COPPER_HOE, Items.COPPER_PICKAXE, Items.COPPER_SWORD, Items.COPPER_SHOVEL, Items.COPPER_SPEAR);
   }
}
