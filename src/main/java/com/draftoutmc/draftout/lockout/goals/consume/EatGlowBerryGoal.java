package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatGlowBerryGoal extends ConsumeItemGoal {
   public EatGlowBerryGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat a Glow Berry";
   }

   public Item getItem() {
      return Items.GLOW_BERRIES;
   }
}
