package com.draftoutmc.draftout.lockout.goals.craft;

import com.draftoutmc.draftout.lockout.interfaces.CraftUniqueItemsGoal;

public class Craft20UniqueItemsGoal extends CraftUniqueItemsGoal {
   public Craft20UniqueItemsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 20;
   }
}
