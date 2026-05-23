package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatPumpkinPieGoal extends ConsumeItemGoal {
   public EatPumpkinPieGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat Pumpkin Pie";
   }

   public Item getItem() {
      return Items.PUMPKIN_PIE;
   }
}
