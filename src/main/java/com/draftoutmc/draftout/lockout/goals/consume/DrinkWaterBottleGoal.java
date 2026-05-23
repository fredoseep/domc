package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.DrinkPotionGoal;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class DrinkWaterBottleGoal extends DrinkPotionGoal {
   public DrinkWaterBottleGoal(String id, String data) {
      super(id, data);
   }

   public Holder<Potion> getPotion() {
      return Potions.WATER;
   }

   public String getGoalName() {
      return "Drink Water Bottle";
   }
}
