package com.draftoutmc.draftout.lockout.goals.consume.unique;

import com.draftoutmc.draftout.lockout.interfaces.EatUniqueFoodsGoal;

public class Eat10UniqueFoodsGoal extends EatUniqueFoodsGoal {
   public Eat10UniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat 10 Unique Food";
   }

   public int getAmount() {
      return 10;
   }
}
