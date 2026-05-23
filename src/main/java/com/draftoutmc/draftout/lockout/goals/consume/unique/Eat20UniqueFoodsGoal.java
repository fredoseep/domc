package com.draftoutmc.draftout.lockout.goals.consume.unique;

import com.draftoutmc.draftout.lockout.interfaces.EatUniqueFoodsGoal;

public class Eat20UniqueFoodsGoal extends EatUniqueFoodsGoal {
   public Eat20UniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Eat 20 Unique Food";
   }

   public int getAmount() {
      return 20;
   }
}
