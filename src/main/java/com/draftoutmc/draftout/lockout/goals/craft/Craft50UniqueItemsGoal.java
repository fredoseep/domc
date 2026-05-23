package com.draftoutmc.draftout.lockout.goals.craft;

import com.draftoutmc.draftout.lockout.interfaces.CraftUniqueItemsGoal;

public class Craft50UniqueItemsGoal extends CraftUniqueItemsGoal {
   public Craft50UniqueItemsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 50;
   }
}
