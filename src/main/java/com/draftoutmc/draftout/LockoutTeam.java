package com.draftoutmc.draftout;

import java.util.List;

import com.draftoutmc.draftout.lockout.Goal;
import lombok.Generated;

public class LockoutTeam {
   private final List<String> players;
   private final int color;
   private boolean forfeited = false;

   public LockoutTeam(List<String> playerNames, int color) {
      this.players = playerNames;
      this.color = color;
   }

   public List<String> getPlayerNames() {
      return this.players;
   }

   public String getDisplayName() {
      return (String)this.players.getFirst();
   }

   public int getPoints(Lockout lockout) {
      if (!Lockout.exists(lockout)) {
         return 0;
      } else {
         int points = 0;

         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal.isCompleted() && this.equals(goal.getCompletedTeam())) {
               ++points;
            }
         }

         return points;
      }
   }

   @Generated
   public int getColor() {
      return this.color;
   }


   @Generated
   public boolean isForfeited() {
      return this.forfeited;
   }

   @Generated
   public void setForfeited(boolean forfeited) {
      this.forfeited = forfeited;
   }
}
