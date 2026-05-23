package com.draftoutmc.draftout.client;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.GoalRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import oshi.util.tuples.Pair;

public class LockoutBoard {
   private final int size;
   private final List<Goal> goals;

   public LockoutBoard(List<Pair<String, String>> goals) {
      this.size = (int)Math.sqrt((double)goals.size());
      if (goals.size() == this.size * this.size && this.size >= 3 && this.size <= 7) {
         this.goals = toGoals(goals);
      } else {
         throw new IllegalArgumentException(String.format("Invalid number of goals (%d)", goals.size()));
      }
   }

   public List<Goal> getGoals() {
      return Collections.unmodifiableList(this.goals);
   }

   public static List<Goal> toGoals(List<Pair<String, String>> goals) {
      List<Goal> goalList = new ArrayList();

      for(Pair<String, String> goal : goals) {
         if ("null".equals(goal.getA())) {
            goalList.add((Goal) (Object)null);
         } else {
            goalList.add(GoalRegistry.INSTANCE.newGoal((String)goal.getA(), (String)goal.getB()));
         }
      }

      return goalList;
   }

   @Generated
   public int size() {
      return this.size;
   }
}
