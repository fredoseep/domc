package com.draftoutmc.draftout.match.data;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutDraftScreen;
import com.draftoutmc.draftout.match.MatchType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.Identifier;
import oshi.util.tuples.Pair;

public class LockoutMatchData {
   public static LockoutMatchData CURRENT_MATCH;
   public static Lockout lockout;
   private final List<LockoutMatchPlayer> players;
   private final LockoutMatchPlayer player;
   private final LockoutMatchPlayer opponent;
   private final MatchType matchType;
   private long startingWorldGenAt;
   public List<Pair<String, String>> goals = new ArrayList();
   private boolean draftBegun;
   private LockoutDraftScreen draftScreen;
   public LockoutBoard pickedBoard;
   private UUID picksNext;
   private long pickEndsAt;
   private long pickTimeMs;
   private int draftIdx = -1;
   private boolean picked;
   private long worldSeed;
   private boolean gotGameData;
   private List<PlayerInfo> playerInfos;
   private boolean gotBoard;
   private boolean requestedDrawVote;
   private boolean forfeited;
   private boolean isRejoin;

   public static LockoutMatchData fromJsonObject(JsonObject msg) {
      List<LockoutMatchPlayer> players = new ArrayList();
      LockoutMatchPlayer player = null;
      LockoutMatchPlayer opponent = null;
      JsonArray playersArray = msg.get("players").getAsJsonArray();

      for(JsonElement playerElement : playersArray) {
         JsonObject playerObject = (JsonObject)playerElement;
         LockoutMatchPlayer matchPlayer = LockoutMatchData.LockoutMatchPlayer.fromJsonObject(playerObject);
         players.add(matchPlayer);
         if (matchPlayer.uuid() == Minecraft.getInstance().getUser().getProfileId()) {
            player = matchPlayer;
         } else if (playersArray.size() == 2) {
            opponent = matchPlayer;
         }
      }

      return new LockoutMatchData(players, player, opponent, MatchType.valueOf(msg.get("matchType").getAsString().toUpperCase()));
   }

   public static List<PlayerInfo> fromPlayers(List<LockoutMatchPlayer> players) {
      List<PlayerInfo> playerInfos = new ArrayList();

      for(LockoutMatchPlayer player : players) {
         GameProfile gameProfile = (GameProfile)Minecraft.getInstance().services().profileResolver().fetchById(player.uuid()).orElse((GameProfile) null);
         if (gameProfile != null) {
            playerInfos.add(new PlayerInfo(gameProfile, false));
         }
      }

      return playerInfos;
   }

   public LockoutMatchPlayer getPlayer(UUID uuid) {
      return (LockoutMatchPlayer)this.players.stream().filter((player) -> player.uuid().equals(uuid)).findFirst().get();
   }

   public static boolean isInMatch() {
      return CURRENT_MATCH != null;
   }

   public static boolean isInMatchWorld() {
      return isInMatch() && CURRENT_MATCH.gotGameData();
   }

   public static void removeActiveMatch() {
      CURRENT_MATCH = null;
   }

   @Generated
   public LockoutMatchData(List<LockoutMatchPlayer> players, LockoutMatchPlayer player, LockoutMatchPlayer opponent, MatchType matchType) {
      this.players = players;
      this.player = player;
      this.opponent = opponent;
      this.matchType = matchType;
   }

   @Generated
   public List<LockoutMatchPlayer> players() {
      return this.players;
   }

   @Generated
   public LockoutMatchPlayer player() {
      return this.player;
   }

   @Generated
   public LockoutMatchPlayer opponent() {
      return this.opponent;
   }

   @Generated
   public MatchType matchType() {
      return this.matchType;
   }

   @Generated
   public long startingWorldGenAt() {
      return this.startingWorldGenAt;
   }

   @Generated
   public List<Pair<String, String>> goals() {
      return this.goals;
   }

   @Generated
   public boolean draftBegun() {
      return this.draftBegun;
   }

   @Generated
   public LockoutDraftScreen draftScreen() {
      return this.draftScreen;
   }

   @Generated
   public LockoutBoard pickedBoard() {
      return this.pickedBoard;
   }

   @Generated
   public UUID picksNext() {
      return this.picksNext;
   }

   @Generated
   public long pickEndsAt() {
      return this.pickEndsAt;
   }

   @Generated
   public long pickTimeMs() {
      return this.pickTimeMs;
   }

   @Generated
   public int draftIdx() {
      return this.draftIdx;
   }

   @Generated
   public boolean picked() {
      return this.picked;
   }

   @Generated
   public long worldSeed() {
      return this.worldSeed;
   }

   @Generated
   public boolean gotGameData() {
      return this.gotGameData;
   }

   @Generated
   public List<PlayerInfo> playerInfos() {
      return this.playerInfos;
   }

   @Generated
   public boolean gotBoard() {
      return this.gotBoard;
   }

   @Generated
   public boolean requestedDrawVote() {
      return this.requestedDrawVote;
   }

   @Generated
   public boolean forfeited() {
      return this.forfeited;
   }

   @Generated
   public boolean isRejoin() {
      return this.isRejoin;
   }

   @Generated
   public LockoutMatchData startingWorldGenAt(long startingWorldGenAt) {
      this.startingWorldGenAt = startingWorldGenAt;
      return this;
   }

   @Generated
   public LockoutMatchData goals(List<Pair<String, String>> goals) {
      this.goals = goals;
      return this;
   }

   @Generated
   public LockoutMatchData draftBegun(boolean draftBegun) {
      this.draftBegun = draftBegun;
      return this;
   }

   @Generated
   public LockoutMatchData draftScreen(LockoutDraftScreen draftScreen) {
      this.draftScreen = draftScreen;
      return this;
   }

   @Generated
   public LockoutMatchData pickedBoard(LockoutBoard pickedBoard) {
      this.pickedBoard = pickedBoard;
      return this;
   }

   @Generated
   public LockoutMatchData picksNext(UUID picksNext) {
      this.picksNext = picksNext;
      return this;
   }

   @Generated
   public LockoutMatchData pickEndsAt(long pickEndsAt) {
      this.pickEndsAt = pickEndsAt;
      return this;
   }

   @Generated
   public LockoutMatchData pickTimeMs(long pickTimeMs) {
      this.pickTimeMs = pickTimeMs;
      return this;
   }

   @Generated
   public LockoutMatchData draftIdx(int draftIdx) {
      this.draftIdx = draftIdx;
      return this;
   }

   @Generated
   public LockoutMatchData picked(boolean picked) {
      this.picked = picked;
      return this;
   }

   @Generated
   public LockoutMatchData worldSeed(long worldSeed) {
      this.worldSeed = worldSeed;
      return this;
   }

   @Generated
   public LockoutMatchData gotGameData(boolean gotGameData) {
      this.gotGameData = gotGameData;
      return this;
   }

   @Generated
   public LockoutMatchData playerInfos(List<PlayerInfo> playerInfos) {
      this.playerInfos = playerInfos;
      return this;
   }

   @Generated
   public LockoutMatchData gotBoard(boolean gotBoard) {
      this.gotBoard = gotBoard;
      return this;
   }

   @Generated
   public LockoutMatchData requestedDrawVote(boolean requestedDrawVote) {
      this.requestedDrawVote = requestedDrawVote;
      return this;
   }

   @Generated
   public LockoutMatchData forfeited(boolean forfeited) {
      this.forfeited = forfeited;
      return this;
   }

   @Generated
   public LockoutMatchData isRejoin(boolean isRejoin) {
      this.isRejoin = isRejoin;
      return this;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LockoutMatchData)) {
         return false;
      } else {
         LockoutMatchData other = (LockoutMatchData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.startingWorldGenAt() != other.startingWorldGenAt()) {
            return false;
         } else if (this.draftBegun() != other.draftBegun()) {
            return false;
         } else if (this.pickEndsAt() != other.pickEndsAt()) {
            return false;
         } else if (this.pickTimeMs() != other.pickTimeMs()) {
            return false;
         } else if (this.draftIdx() != other.draftIdx()) {
            return false;
         } else if (this.picked() != other.picked()) {
            return false;
         } else if (this.worldSeed() != other.worldSeed()) {
            return false;
         } else if (this.gotGameData() != other.gotGameData()) {
            return false;
         } else if (this.gotBoard() != other.gotBoard()) {
            return false;
         } else if (this.requestedDrawVote() != other.requestedDrawVote()) {
            return false;
         } else if (this.forfeited() != other.forfeited()) {
            return false;
         } else if (this.isRejoin() != other.isRejoin()) {
            return false;
         } else {
            Object this$players = this.players();
            Object other$players = other.players();
            if (this$players == null) {
               if (other$players != null) {
                  return false;
               }
            } else if (!this$players.equals(other$players)) {
               return false;
            }

            Object this$player = this.player();
            Object other$player = other.player();
            if (this$player == null) {
               if (other$player != null) {
                  return false;
               }
            } else if (!this$player.equals(other$player)) {
               return false;
            }

            Object this$opponent = this.opponent();
            Object other$opponent = other.opponent();
            if (this$opponent == null) {
               if (other$opponent != null) {
                  return false;
               }
            } else if (!this$opponent.equals(other$opponent)) {
               return false;
            }

            Object this$matchType = this.matchType();
            Object other$matchType = other.matchType();
            if (this$matchType == null) {
               if (other$matchType != null) {
                  return false;
               }
            } else if (!this$matchType.equals(other$matchType)) {
               return false;
            }

            Object this$goals = this.goals();
            Object other$goals = other.goals();
            if (this$goals == null) {
               if (other$goals != null) {
                  return false;
               }
            } else if (!this$goals.equals(other$goals)) {
               return false;
            }

            Object this$draftScreen = this.draftScreen();
            Object other$draftScreen = other.draftScreen();
            if (this$draftScreen == null) {
               if (other$draftScreen != null) {
                  return false;
               }
            } else if (!this$draftScreen.equals(other$draftScreen)) {
               return false;
            }

            Object this$pickedBoard = this.pickedBoard();
            Object other$pickedBoard = other.pickedBoard();
            if (this$pickedBoard == null) {
               if (other$pickedBoard != null) {
                  return false;
               }
            } else if (!this$pickedBoard.equals(other$pickedBoard)) {
               return false;
            }

            Object this$picksNext = this.picksNext();
            Object other$picksNext = other.picksNext();
            if (this$picksNext == null) {
               if (other$picksNext != null) {
                  return false;
               }
            } else if (!this$picksNext.equals(other$picksNext)) {
               return false;
            }

            Object this$playerInfos = this.playerInfos();
            Object other$playerInfos = other.playerInfos();
            if (this$playerInfos == null) {
               if (other$playerInfos != null) {
                  return false;
               }
            } else if (!this$playerInfos.equals(other$playerInfos)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof LockoutMatchData;
   }

   @Generated
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      long $startingWorldGenAt = this.startingWorldGenAt();
      result = result * 59 + (int)($startingWorldGenAt >>> 32 ^ $startingWorldGenAt);
      result = result * 59 + (this.draftBegun() ? 79 : 97);
      long $pickEndsAt = this.pickEndsAt();
      result = result * 59 + (int)($pickEndsAt >>> 32 ^ $pickEndsAt);
      long $pickTimeMs = this.pickTimeMs();
      result = result * 59 + (int)($pickTimeMs >>> 32 ^ $pickTimeMs);
      result = result * 59 + this.draftIdx();
      result = result * 59 + (this.picked() ? 79 : 97);
      long $worldSeed = this.worldSeed();
      result = result * 59 + (int)($worldSeed >>> 32 ^ $worldSeed);
      result = result * 59 + (this.gotGameData() ? 79 : 97);
      result = result * 59 + (this.gotBoard() ? 79 : 97);
      result = result * 59 + (this.requestedDrawVote() ? 79 : 97);
      result = result * 59 + (this.forfeited() ? 79 : 97);
      result = result * 59 + (this.isRejoin() ? 79 : 97);
      Object $players = this.players();
      result = result * 59 + ($players == null ? 43 : $players.hashCode());
      Object $player = this.player();
      result = result * 59 + ($player == null ? 43 : $player.hashCode());
      Object $opponent = this.opponent();
      result = result * 59 + ($opponent == null ? 43 : $opponent.hashCode());
      Object $matchType = this.matchType();
      result = result * 59 + ($matchType == null ? 43 : $matchType.hashCode());
      Object $goals = this.goals();
      result = result * 59 + ($goals == null ? 43 : $goals.hashCode());
      Object $draftScreen = this.draftScreen();
      result = result * 59 + ($draftScreen == null ? 43 : $draftScreen.hashCode());
      Object $pickedBoard = this.pickedBoard();
      result = result * 59 + ($pickedBoard == null ? 43 : $pickedBoard.hashCode());
      Object $picksNext = this.picksNext();
      result = result * 59 + ($picksNext == null ? 43 : $picksNext.hashCode());
      Object $playerInfos = this.playerInfos();
      result = result * 59 + ($playerInfos == null ? 43 : $playerInfos.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.players());
      return "LockoutMatchData(players=" + var10000 + ", player=" + String.valueOf(this.player()) + ", opponent=" + String.valueOf(this.opponent()) + ", matchType=" + String.valueOf(this.matchType()) + ", startingWorldGenAt=" + this.startingWorldGenAt() + ", goals=" + String.valueOf(this.goals()) + ", draftBegun=" + this.draftBegun() + ", draftScreen=" + String.valueOf(this.draftScreen()) + ", pickedBoard=" + String.valueOf(this.pickedBoard()) + ", picksNext=" + String.valueOf(this.picksNext()) + ", pickEndsAt=" + this.pickEndsAt() + ", pickTimeMs=" + this.pickTimeMs() + ", draftIdx=" + this.draftIdx() + ", picked=" + this.picked() + ", worldSeed=" + this.worldSeed() + ", gotGameData=" + this.gotGameData() + ", playerInfos=" + String.valueOf(this.playerInfos()) + ", gotBoard=" + this.gotBoard() + ", requestedDrawVote=" + this.requestedDrawVote() + ", forfeited=" + this.forfeited() + ", isRejoin=" + this.isRejoin() + ")";
   }

   @Generated
   public static Lockout getLockout() {
      return lockout;
   }

   @Generated
   public static void setLockout(Lockout lockout) {
      LockoutMatchData.lockout = lockout;
   }

   public static class LockoutMatchPlayer extends PlayerProfile {
      private final LockoutTeamServer lockoutTeamServer;
      private String dimension;

      public LockoutMatchPlayer(UUID uuid, String username, int elo, boolean ranked, int rank, Identifier skin, int preferredColor1, int preferredColor2, LockoutTeamServer lockoutTeamServer, String dimension) {
         super(uuid, username, elo, ranked, rank, skin, preferredColor1, preferredColor2);
         this.lockoutTeamServer = lockoutTeamServer;
         this.dimension = dimension;
      }

      public static LockoutMatchPlayer fromJsonObject(JsonObject playerObject) {
         String username = playerObject.get("username").getAsString();
         UUID uuid = UUID.fromString(playerObject.get("uuid").getAsString());
         Identifier skin = SKIN_RENDERER.getSkinTexture(uuid, playerObject.get("skin") != null && !playerObject.get("skin").isJsonNull() ? playerObject.get("skin").getAsString() : null);
         JsonElement dimension = playerObject.get("dimension");
         JsonElement color = playerObject.get("color");
         return new LockoutMatchPlayer(uuid, username, playerObject.get("elo").getAsInt(), playerObject.get("ranked").getAsBoolean(), playerObject.get("rank") == null ? -1 : playerObject.get("rank").getAsInt(), skin, playerObject.get("preferredColor1").getAsInt(), playerObject.get("preferredColor2").getAsInt(), new LockoutTeamServer(Collections.singletonMap(username, uuid), color == null ? -1 : color.getAsInt()), dimension == null ? "overworld" : dimension.getAsString());
      }

      @Generated
      public LockoutTeamServer lockoutTeamServer() {
         return this.lockoutTeamServer;
      }

      @Generated
      public String dimension() {
         return this.dimension;
      }

      @Generated
      public LockoutMatchPlayer dimension(String dimension) {
         this.dimension = dimension;
         return this;
      }
   }
}
