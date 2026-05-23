package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatSuspiciousStewGoal extends ConsumeItemGoal {
   public EatSuspiciousStewGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat Suspicious Stew";
   }

   public Item getItem() {
      return Items.SUSPICIOUS_STEW;
   }
}
