package com.draftoutmc.draftout.match;

import com.draftoutmc.draftout.match.data.LockoutMatchData;

public class RankedUtils {
   public static boolean isInDraftPhase() {
      if (LockoutMatchData.isInMatch()) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         if (matchData.matchType() != MatchType.COMPETITIVE) {
            return false;
         } else {
            return LockoutMatchData.getLockout() == null;
         }
      } else {
         return false;
      }
   }
}
