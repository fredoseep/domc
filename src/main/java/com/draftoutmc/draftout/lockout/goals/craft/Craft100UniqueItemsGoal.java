package com.draftoutmc.draftout.lockout.goals.craft;

import com.draftoutmc.draftout.lockout.interfaces.CraftUniqueItemsGoal;

public class Craft100UniqueItemsGoal extends CraftUniqueItemsGoal {
   public Craft100UniqueItemsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 100;
   }
}
