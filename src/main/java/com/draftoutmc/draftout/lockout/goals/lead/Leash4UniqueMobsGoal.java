package com.draftoutmc.draftout.lockout.goals.lead;

import com.draftoutmc.draftout.lockout.interfaces.LeashUniqueMobsGoal;

public class Leash4UniqueMobsGoal extends LeashUniqueMobsGoal {
   public Leash4UniqueMobsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 4;
   }
}
