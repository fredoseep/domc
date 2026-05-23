package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.lockout.interfaces.WearArmorGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class WearDiamondArmorGoal extends WearArmorGoal {
   private static final List<Item> ITEMS;

   public WearDiamondArmorGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear Full Diamond Armor";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   static {
      ITEMS = List.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
   }
}
