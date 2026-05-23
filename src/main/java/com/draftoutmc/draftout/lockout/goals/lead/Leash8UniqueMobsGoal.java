package com.draftoutmc.draftout.lockout.goals.lead;

import com.draftoutmc.draftout.lockout.interfaces.LeashUniqueMobsGoal;

public class Leash8UniqueMobsGoal extends LeashUniqueMobsGoal {
   public Leash8UniqueMobsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 8;
   }
}
