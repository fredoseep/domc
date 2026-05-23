package com.draftoutmc.draftout.lockout.goals.consume.unique;

import com.draftoutmc.draftout.lockout.interfaces.EatUniqueFoodsGoal;

public class Eat15UniqueFoodsGoal extends EatUniqueFoodsGoal {
   public Eat15UniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat 15 Unique Food";
   }

   public int getAmount() {
      return 15;
   }
}
