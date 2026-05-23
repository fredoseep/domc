package com.draftoutmc.draftout.match.data;

import lombok.Generated;

public enum MatchOutcome {
   FINISHED("Finished"),
   FORFEITED("Forfeited"),
   CANCELLED("Cancelled"),
   DRAW("Draw"),
   DRAW_BY_VOTE("Draw");

   private final String display;

   private MatchOutcome(String display) {
      this.display = display;
   }

   public static MatchOutcome match(String s) {
      for(MatchOutcome value : values()) {
         if (value.name().equals(s.toUpperCase())) {
            return value;
         }
      }

      return null;
   }

   public static boolean isDraw(MatchOutcome outcome) {
      return outcome == DRAW || outcome == DRAW_BY_VOTE;
   }

   @Generated
   public String getDisplay() {
      return this.display;
   }

   // $FF: synthetic method
   private static MatchOutcome[] $values() {
      return new MatchOutcome[]{FINISHED, FORFEITED, CANCELLED, DRAW, DRAW_BY_VOTE};
   }
}
