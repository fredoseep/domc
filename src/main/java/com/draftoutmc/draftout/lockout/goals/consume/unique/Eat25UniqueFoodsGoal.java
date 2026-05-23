package com.draftoutmc.draftout.lockout.goals.consume.unique;

import com.draftoutmc.draftout.lockout.interfaces.EatUniqueFoodsGoal;

public class Eat25UniqueFoodsGoal extends EatUniqueFoodsGoal {
   public Eat25UniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat 25 Unique Food";
   }

   public int getAmount() {
      return 25;
   }
}
