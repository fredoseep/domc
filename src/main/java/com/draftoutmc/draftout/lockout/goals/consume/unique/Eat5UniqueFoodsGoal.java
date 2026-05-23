package com.draftoutmc.draftout.lockout.goals.consume.unique;

import com.draftoutmc.draftout.lockout.interfaces.EatUniqueFoodsGoal;

public class Eat5UniqueFoodsGoal extends EatUniqueFoodsGoal {
   public Eat5UniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat 5 Unique Food";
   }

   public int getAmount() {
      return 5;
   }
}
