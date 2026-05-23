package com.draftoutmc.draftout.match.data;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.lockout.Goal;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oshi.util.tuples.Pair;

public class MatchHistoryData {
   public static MatchPage parse(JsonObject msg) {
      int page = msg.get("page").getAsInt();
      int totalPages = msg.get("totalPages").getAsInt();
      String playerId = msg.get("playerId").getAsString();
      List<MatchResult> results = new ArrayList();

      for(JsonElement matchEl : msg.get("matches").getAsJsonArray()) {
         JsonObject match = matchEl.getAsJsonObject();
         int id = match.get("id").getAsInt();
         long completedAt = match.get("completedAt").getAsLong();
         String matchType = match.get("matchType").getAsString();
         int durationMs = match.get("duration").getAsInt();
         String outcome = match.get("outcome").getAsString();
         JsonArray board = match.get("finalBoard").getAsJsonArray();
         Map<String, PlayerResult> players = new HashMap();
         Map<String, LockoutTeam> lockoutTeamMap = new HashMap();

         for(JsonElement playerEl : match.get("players").getAsJsonArray()) {
            JsonObject player = playerEl.getAsJsonObject();
            boolean won = player.get("won").getAsBoolean();
            String name = player.get("name").getAsString();
            String uuid = player.get("uuid").getAsString();
            int color = player.get("color").getAsInt();
            int score = 0;

            for(JsonElement goalEl : board) {
               JsonObject goal = goalEl.getAsJsonObject();
               if (goal.get("completed").getAsBoolean() && goal.get("completedByUuid").getAsString().equals(uuid)) {
                  ++score;
               }
            }

            int eloBefore = matchType.equals("draftout") && !Utility.isJsonNull(player.get("eloBefore")) ? player.get("eloBefore").getAsInt() : -1;
            int eloChange = matchType.equals("draftout") && !Utility.isJsonNull(player.get("eloChange")) ? player.get("eloChange").getAsInt() : -1;
            lockoutTeamMap.put(uuid, new LockoutTeam(Collections.singletonList(name), color));
            players.put(uuid, new PlayerResult(name, uuid, won, score, eloBefore, eloChange));
         }

         List<Pair<String, String>> boardList = new ArrayList();

         for(JsonElement goalEl : match.get("finalBoard").getAsJsonArray()) {
            JsonObject goal = goalEl.getAsJsonObject();
            String goalId = goal.get("id").getAsString();
            String goalData = goal.get("data").getAsString();
            boardList.add(new Pair(goalId, goalData));
         }

         LockoutBoard lockoutBoard = new LockoutBoard(boardList.subList(0, 25));
         int i = 0;

         for(JsonElement goalEl : match.get("finalBoard").getAsJsonArray()) {
            JsonObject goal = goalEl.getAsJsonObject();
            boolean completed = goal.get("completed").getAsBoolean();
            if (completed) {
               Goal g = (Goal)lockoutBoard.getGoals().get(i);
               String completedByUuid = goal.get("completedByUuid").getAsString();
               g.setCompleted(true, (LockoutTeam)lockoutTeamMap.get(completedByUuid));
               long completedAtMs = goal.get("completedAtMs").getAsLong();
               g.setCompletedAtMs(completedAtMs);
            }

            ++i;
         }

         results.add(new MatchResult(id, completedAt, matchType, durationMs, MatchOutcome.match(outcome), players, lockoutTeamMap, lockoutBoard));
      }

      Map<String, PlayerProfile> profiles = new HashMap();
      if (!Utility.isJsonNull(msg.get("profiles"))) {
         JsonObject profilesObject = msg.get("profiles").getAsJsonObject();

         for(String uuidString : profilesObject.keySet()) {
            profiles.put(uuidString, PlayerProfile.parse(profilesObject.get(uuidString).getAsJsonObject()));
         }
      }

      return new MatchPage(page, totalPages, playerId, results, profiles);
   }

   public static record PlayerResult(String name, String uuid, boolean won, int score, int eloBefore, int eloChange) {
   }

   public static record MatchResult(int id, long completedAt, String matchType, int durationMs, MatchOutcome outcome, Map<String, PlayerResult> players, Map<String, LockoutTeam> lockoutTeamMap, LockoutBoard board) {
   }

   public static record MatchPage(int page, int totalPages, String playerId, List<MatchResult> matches, Map<String, PlayerProfile> profiles) {
   }
}
