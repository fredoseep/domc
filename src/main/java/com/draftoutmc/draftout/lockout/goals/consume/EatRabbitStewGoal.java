package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatRabbitStewGoal extends ConsumeItemGoal {
   public EatRabbitStewGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat Rabbit Stew";
   }

   public Item getItem() {
      return Items.RABBIT_STEW;
   }
}
