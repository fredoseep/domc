package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.lockout.interfaces.WearArmorGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class WearCopperArmorGoal extends WearArmorGoal {
   private static final List<Item> ITEMS;

   public WearCopperArmorGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear Full Copper Armor";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   static {
      ITEMS = List.of(Items.COPPER_HELMET, Items.COPPER_CHESTPLATE, Items.COPPER_LEGGINGS, Items.COPPER_BOOTS);
   }
}
