package com.draftoutmc.draftout.match;

import lombok.Generated;

public enum MatchType {
   QUICK_PLAY("Quick Play"),
   COMPETITIVE("Competitive"),
   DRAFTOUT("Competitive");

   private final String displayName;

   private MatchType(String displayName) {
      this.displayName = displayName;
   }

   public String toString() {
      return this.name().toLowerCase();
   }

   public static MatchType match(String type) {
      for(MatchType value : values()) {
         if (value.name().equals(type.toUpperCase())) {
            return value;
         }
      }

      return null;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }

   // $FF: synthetic method
   private static MatchType[] $values() {
      return new MatchType[]{QUICK_PLAY, COMPETITIVE, DRAFTOUT};
   }
}
