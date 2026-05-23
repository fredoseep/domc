package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class DrinkHoneyBottleGoal extends ConsumeItemGoal {
   public DrinkHoneyBottleGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Drink Honey Bottle";
   }

   public Item getItem() {
      return Items.HONEY_BOTTLE;
   }
}
