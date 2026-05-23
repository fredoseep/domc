package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatPoisonousPotatoGoal extends ConsumeItemGoal {
   public EatPoisonousPotatoGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat a Poisonous Potato";
   }

   public Item getItem() {
      return Items.POISONOUS_POTATO;
   }
}
