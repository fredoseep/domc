package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatChorusFruitGoal extends ConsumeItemGoal {
   public EatChorusFruitGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat Chorus Fruit";
   }

   public Item getItem() {
      return Items.CHORUS_FRUIT;
   }
}
