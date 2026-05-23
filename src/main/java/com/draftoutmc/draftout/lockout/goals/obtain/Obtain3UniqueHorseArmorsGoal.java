package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain3UniqueHorseArmorsGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public Obtain3UniqueHorseArmorsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 3 types of Horse Armor";
   }

   public int getAmount() {
      return 3;
   }

   static {
      ITEMS = List.of(Items.IRON_HORSE_ARMOR, Items.LEATHER_HORSE_ARMOR, Items.DIAMOND_HORSE_ARMOR, Items.GOLDEN_HORSE_ARMOR, Items.COPPER_HORSE_ARMOR, Items.NETHERITE_HORSE_ARMOR);
   }
}
