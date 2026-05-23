package com.draftoutmc.draftout.lockout.goals.compost;

import com.draftoutmc.draftout.lockout.interfaces.CompostUniqueFoodsGoal;

public class Compost3UniqueFoodsGoal extends CompostUniqueFoodsGoal {
   public Compost3UniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 3;
   }
}
