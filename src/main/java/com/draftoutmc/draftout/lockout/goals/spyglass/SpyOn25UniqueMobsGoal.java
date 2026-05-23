package com.draftoutmc.draftout.lockout.goals.spyglass;

import com.draftoutmc.draftout.lockout.interfaces.SpyOnUniqueMobsGoal;

public class SpyOn25UniqueMobsGoal extends SpyOnUniqueMobsGoal {
   public SpyOn25UniqueMobsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 25;
   }
}
