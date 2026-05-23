package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.lockout.interfaces.WearArmorGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class WearGoldenArmorGoal extends WearArmorGoal {
   private static final List<Item> ITEMS;

   public WearGoldenArmorGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear Full Golden Armor";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   static {
      ITEMS = List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
   }
}
