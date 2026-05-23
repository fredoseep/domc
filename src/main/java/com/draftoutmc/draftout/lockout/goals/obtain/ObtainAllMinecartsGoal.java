package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainAllMinecartsGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainAllMinecartsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain every type of Minecart";
   }

   static {
      ITEMS = List.of(Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.HOPPER_MINECART, Items.TNT_MINECART);
   }
}
