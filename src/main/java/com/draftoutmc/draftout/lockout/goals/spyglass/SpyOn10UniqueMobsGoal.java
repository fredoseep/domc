package com.draftoutmc.draftout.lockout.goals.spyglass;

import com.draftoutmc.draftout.lockout.interfaces.SpyOnUniqueMobsGoal;

public class SpyOn10UniqueMobsGoal extends SpyOnUniqueMobsGoal {
   public SpyOn10UniqueMobsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 10;
   }
}
