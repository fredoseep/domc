package com.draftoutmc.draftout.websocket;

import com.draftoutmc.draftout.*;
import com.draftoutmc.draftout.client.TooltipCache;
import com.draftoutmc.draftout.client.gui.ranked.elements.FadeToScreenTransition;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutInformationScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutMainScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.PostGameScreen;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.MinecraftUtils;
import com.draftoutmc.draftout.match.WorldCreator;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.RankedData;
import com.draftoutmc.draftout.server.LockoutServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.GameType;
import oshi.util.tuples.Pair;

public class GameMessageHandler {
   private ServerConnection sc;

   public GameMessageHandler(ServerConnection sc) {
      this.sc = sc;
   }

   public void handle(String type, JsonObject msg) {
      switch (type) {
         case "join_queue" -> this.handleJoinedQueue(msg);
         case "found" -> this.handleMatchFound(msg);
         case "data" -> this.handleGameData(msg);
         case "set_goals" -> this.handleSetGoals(msg);
         case "start_lockout" -> this.handleStartLockout(msg);
         case "chat" -> this.handleChat(msg);
         case "advancement" -> this.handleAdvancement(msg);
         case "goal_completed" -> this.handleGoalCompleted(msg);
         case "finish" -> this.handleFinished(msg);
         case "cancelled" -> this.handleCancelled(msg);
         case "forfeit" -> this.handleForfeit(msg);
         case "rematch_request_started" -> this.handleRematchRequestStarted(msg);
         case "rematch_accepted" -> this.handleRematchAccepted(msg);
         case "draw_vote_started" -> this.handleDrawVoteStarted(msg);
         case "draw_vote_expired" -> this.handleDrawVoteExpired(msg);
         case "forfeited" -> this.handleForfeited(msg);
         case "dimension" -> this.handleUpdateDimension(msg);
         case "player_left" -> this.handlePlayerLeft(msg);
         case "player_disconnected" -> this.handlePlayerDisconnected(msg);
         case "player_reconnected" -> this.handlePlayerReconnected(msg);
         case "screen_data" -> this.handleScreenData(msg);
         case "rejoin_world" -> this.handleRejoinWorld(msg);
         case "rejoin_state" -> this.handleRejoinState(msg);
      }

   }

   private void handleJoinedQueue(JsonObject msg) {
      if (!msg.get("joined").getAsBoolean()) {
         ServerConnection.getInstance().queueType = null;
         ServerConnection.getInstance().queueStartTimeMs = 0L;
         Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(new LockoutInformationScreen(msg.get("reason").getAsString(), Component.literal("Could not join queue"), new LockoutMainScreen(), "Back to Main Menu"));
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
         });
      }

   }

   private void handleMatchFound(JsonObject msg) {
      this.sc.queueType = null;
      this.sc.queueStartTimeMs = 0L;
      MinecraftUtils.notMinecraft$execute(() -> {
         LockoutMatchData matchData = LockoutMatchData.fromJsonObject(msg);
         LockoutMatchData.CURRENT_MATCH = matchData;
         matchData.players().stream().filter((player) -> player.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst().ifPresent((me) -> {
            List<LockoutMatchData.LockoutMatchPlayer> players = matchData.players();
            if (players.remove(me)) {
               players.addFirst(me);
            }

         });
         matchData.playerInfos(LockoutMatchData.fromPlayers(matchData.players()));
         matchData.playerInfos().stream().filter((player) -> player.getProfile().id().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst().ifPresent((me) -> {
            List<PlayerInfo> playerInfos = matchData.playerInfos();
            if (playerInfos.remove(me)) {
               playerInfos.addFirst(me);
            }

         });
         matchData.startingWorldGenAt(System.currentTimeMillis() + 5000L);
         List<LockoutTeamServer> teams = new ArrayList();
         LockoutServer.parseIntoTeams(teams, matchData.players());
         Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.AMETHYST_BLOCK_RESONATE, 1.0F, 1.0F));
      });
      Utility.sendScreenDataForTwitchExtension();
   }

   private void handleGameData(JsonObject msg) {
      JsonObject data = msg.get("gameData").getAsJsonObject();
      LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
      if (LockoutMatchData.isInMatch()) {
         matchData.gotGameData(true);
         long worldSeed = data.get("worldSeed").getAsLong();
         matchData.worldSeed(worldSeed);
         matchData.startingWorldGenAt(0L);
         TooltipCache.clearCache();
         MinecraftUtils.notMinecraft$execute(() -> {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUNDLE_DROP_CONTENTS, 0.75F, 2.0F));
            if (Minecraft.getInstance().getConnection() != null) {
               Minecraft.getInstance().disconnectFromWorld(Component.literal("Queue found..."));
            } else {
               Screen patt0$temp = Minecraft.getInstance().screen;
               if (patt0$temp instanceof ConnectScreen) {
                  ConnectScreen connectScreen = (ConnectScreen) patt0$temp;
                  Utility.abortServerConnect(connectScreen);
                  Minecraft.getInstance().setScreen(new LockoutMainScreen());
               }
            }

            String baseName = "lockout-match";
            String uniqueName = WorldCreator.makeUniqueWorldName(baseName);
            WorldCreator.createWorld(uniqueName, worldSeed);
            RankedData.worldName(uniqueName);
         });
      }
   }

   private void handleSetGoals(JsonObject msg) {
      MinecraftUtils.notMinecraft$execute(() -> {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         if (LockoutMatchData.isInMatch()) {
            JsonArray boardArray = msg.get("board").getAsJsonArray();
            matchData.goals = new ArrayList();

            for(JsonElement goal : boardArray) {
               String goalId = goal.getAsJsonObject().get("id").getAsString();
               String goalData = goal.getAsJsonObject().get("data").getAsString();
               matchData.goals.add(new Pair(goalId, goalData));
            }

            LockoutMatchData.CURRENT_MATCH.gotBoard(true);
            Lockout.log("Received custom board from server.");
         }
      });
   }

   private void handleStartLockout(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         if (LockoutMatchData.isInMatch()) {
            long gameTime = msg.get("gameTime").getAsLong();
            boolean rejoinLockout = !Utility.isJsonNull(msg.get("rejoin")) && msg.get("rejoin").getAsBoolean() && RankedData.lockout() != null;
            List<LockoutTeamServer> teams = new ArrayList<>();

            if (!rejoinLockout) {
               for(LockoutMatchData.LockoutMatchPlayer player : LockoutMatchData.CURRENT_MATCH.players()) {
                  teams.add(player.lockoutTeamServer());
               }
            } else {
               teams = (List<LockoutTeamServer>) RankedData.lockout().getTeams();
            }
            LockoutServer.startLockout(teams, matchData.goals, gameTime,rejoinLockout);
         }
      });
   }

   private void handleChat(JsonObject msg) {
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal(msg.get("msg").getAsString()), true));
   }

   private void handleAdvancement(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         String key = msg.get("advancement").getAsString();
         UUID uuid = UUID.fromString(msg.get("completedByUuid").getAsString());
         AdvancementHolder holder = LockoutServer.server.getAdvancements().get(Identifier.parse(key));
         if (holder != null && !holder.value().display().isEmpty()) {
            Lockout lockout = LockoutMatchData.getLockout();
            LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(uuid);
            String playerName = team.getPlayerName(uuid);
            AdvancementType advancementType = ((DisplayInfo)holder.value().display().get()).getType();
            MutableComponent advancementComponent = Component.translatable("chat.type.advancement." + advancementType.getSerializedName(), new Object[]{playerName, Advancement.name(holder)}).copy().withStyle(ChatFormatting.RESET).withColor(ChatFormatting.GRAY.getColor()).withStyle(ChatFormatting.ITALIC);
            Minecraft.getInstance().getChatListener().handleSystemMessage(advancementComponent, true);
         }
      });
   }

   private void handleGoalCompleted(JsonObject msg) {
      LockoutServer.server.execute(() -> {
         int goalIdx = msg.get("goalIndex").getAsInt();
         UUID uuid = UUID.fromString(msg.get("completedByUuid").getAsString());
         Lockout lockout = LockoutMatchData.getLockout();
         Goal goal = (Goal)lockout.getBoard().getGoals().get(goalIdx);
         if (goal.isCompleted()) {
            lockout.clearGoalCompletion(goal, false);
         }

         LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(uuid);
         String var10000 = team.getPlayerName(uuid);
         String message = var10000 + " completed " + goal.getGoalName() + ".";
         lockout.onGoalCompleted(goal, team, message);
      });
   }

   private void handleFinished(JsonObject msg) {
      this.handleGameOver(false, msg);
   }

   private void handleCancelled(JsonObject msg) {
      this.handleGameOver(true, msg);
   }

   private void handleGameOver(boolean cancelled, JsonObject msg) {
      LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
      Lockout lockout = LockoutMatchData.getLockout();
      LockoutServer.RUNNABLES.clear();
      RankedData.worldName((String)null);
      RankedData.lockout((Lockout)null);
      long finalTime;
      if (lockout != null) {
         finalTime = msg.get("finalTime") == null ? System.currentTimeMillis() - lockout.getStartTimeMillis() : msg.get("finalTime").getAsLong();
         lockout.setRunning(false);
         lockout.setGameEndMillis(lockout.getStartTimeMillis() + finalTime);
      } else {
         finalTime = 0L;
      }

      if (Lockout.exists(lockout) && Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
         Minecraft.getInstance().execute(() -> {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal.getPendingChatMessage() != null) {
                  goal.getPendingChatMessage().remove();
               }
            }

            LocalPlayer player = Minecraft.getInstance().player;
            ServerPlayer serverPlayer = LockoutServer.server.getPlayerList().getPlayer(Minecraft.getInstance().getUser().getProfileId());
            serverPlayer.setGameMode(GameType.SPECTATOR);
            Minecraft.getInstance().gui.setTimes(10, 100, 10);
            Minecraft.getInstance().setScreen((Screen)null);
            if (cancelled) {
               Minecraft.getInstance().gui.setTitle(Component.literal("Game cancelled").withColor(ChatFormatting.YELLOW.getColor()).withStyle(ChatFormatting.BOLD));
               Minecraft.getInstance().gui.setSubtitle(Component.literal(msg.get("reason").getAsString()));
               player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 1.0F);
               Map<LockoutMatchData.LockoutMatchPlayer, Integer> eloChanges = new HashMap();

               for(LockoutMatchData.LockoutMatchPlayer lmp : matchData.players()) {
                  eloChanges.put(lmp, 0);
               }

               EditBox chat = null;
               Screen patt0$temp = Minecraft.getInstance().screen;
               if (patt0$temp instanceof ChatScreen) {
                  ChatScreen chatScreen = (ChatScreen)patt0$temp;
                  chat = chatScreen.input;
               }

               Screen postGameScreen = new PostGameScreen(eloChanges, chat, 4, finalTime);
               LockoutRunnable screenTask = () -> {
                  Screen screen = new FadeToScreenTransition(() -> postGameScreen, 2500L);
                  Minecraft.getInstance().setScreen(screen);
               };
               screenTask.runTaskAt(System.currentTimeMillis() + 1500L);
            } else {
               List<UUID> winnerIds = new ArrayList();

               for(JsonElement winnerId : msg.get("winnerIds").getAsJsonArray()) {
                  winnerIds.add(UUID.fromString(winnerId.getAsString()));
               }

               boolean didIWin = false;
               Set<LockoutTeam> winnerTeams = new HashSet();

               for(UUID winnerId : winnerIds) {
                  LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(winnerId);
                  winnerTeams.add(team);
                  if (team.getPlayers().contains(Minecraft.getInstance().getUser().getProfileId())) {
                     didIWin = true;
                     break;
                  }
               }

               boolean draw = winnerTeams.size() != 1;
               if (draw) {
                  if (winnerTeams.isEmpty()) {
                     Minecraft.getInstance().gui.setTitle(Component.literal("Draw!").withColor(ChatFormatting.YELLOW.getColor()).withStyle(ChatFormatting.BOLD));
                     player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 1.0F);
                  } else if (didIWin) {
                     Minecraft.getInstance().gui.setTitle(Component.literal("Draw!").withColor(ChatFormatting.YELLOW.getColor()).withStyle(ChatFormatting.BOLD));
                     player.playSound(SoundEvents.PILLAGER_CELEBRATE, 2.0F, 1.0F);
                  }
               } else if (didIWin) {
                  Minecraft.getInstance().gui.setTitle(Component.literal("Victory!").withColor(ChatFormatting.GREEN.getColor()).withStyle(ChatFormatting.BOLD));
                  player.playSound(SoundEvents.PILLAGER_CELEBRATE, 2.0F, 1.0F);
               } else {
                  Minecraft.getInstance().gui.setTitle(Component.literal("Defeat!").withColor(ChatFormatting.RED.getColor()).withStyle(ChatFormatting.BOLD));
                  player.playSound(SoundEvents.WARDEN_DEATH, 2.0F, 1.0F);
               }

               Map<LockoutMatchData.LockoutMatchPlayer, Integer> eloChanges = new HashMap();

               for(JsonElement playerEloChange : msg.get("eloChanges").getAsJsonArray()) {
                  UUID uuid = UUID.fromString(playerEloChange.getAsJsonObject().get("uuid").getAsString());
                  int eloChange = playerEloChange.getAsJsonObject().get("eloChange").getAsInt();
                  LockoutMatchData.LockoutMatchPlayer lmp = matchData.getPlayer(uuid);
                  eloChanges.put(lmp, eloChange);
               }

               for(LockoutMatchData.LockoutMatchPlayer lockoutMatchPlayer : matchData.players()) {
                  if (!eloChanges.containsKey(lockoutMatchPlayer)) {
                     eloChanges.put(lockoutMatchPlayer, (Integer) null);
                  }
               }

               EditBox chat = null;
               Screen patt0$temp = Minecraft.getInstance().screen;
               if (patt0$temp instanceof ChatScreen) {
                  ChatScreen chatScreen = (ChatScreen)patt0$temp;
                  chat = chatScreen.input;
               }

               Screen postGameScreen = new PostGameScreen(eloChanges, chat, (didIWin ? 1 : 0) + (draw ? 2 : 0), finalTime);
               LockoutRunnable screenTask = () -> {
                  Screen screen = new FadeToScreenTransition(() -> postGameScreen, 2500L);
                  Minecraft.getInstance().setScreen(screen);
               };
               screenTask.runTaskAt(System.currentTimeMillis() + 3000L);
            }

         });
      } else {
         LockoutMatchData.removeActiveMatch();
         Minecraft.getInstance().execute(() -> Minecraft.getInstance().disconnect(new LockoutInformationScreen("Opponent disconnected, game cancelled."), false, true));
      }
   }

   private void handleForfeit(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         LocalPlayer player = Minecraft.getInstance().player;
         if (player != null) {
            UUID uuid = UUID.fromString(msg.get("forfeiterUuid").getAsString());
            Lockout lockout = LockoutMatchData.getLockout();
            LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(uuid);
            String var10000 = team.getPlayerName(uuid);
            String message = var10000 + " forfeited the match.";
            player.sendSystemMessage(Component.literal(message).withColor(ChatFormatting.GRAY.getColor()));
         }

      });
   }

   private void handleRematchRequestStarted(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         if (LockoutMatchData.isInMatch()) {
            LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
            LocalPlayer player = Minecraft.getInstance().player;
            UUID uuid = UUID.fromString(msg.get("startedByUuid").getAsString());
            Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(uuid)).findFirst();
            if (!playerOptional.isEmpty()) {
               String message = ((LockoutMatchData.LockoutMatchPlayer)playerOptional.get()).username() + " wants to rematch!";
               if (player != null) {
                  player.sendSystemMessage(Component.literal(message).withColor(ChatFormatting.GREEN.getColor()));
                  Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 1.0F));
               }

            }
         }
      });
   }

   private void handleRematchAccepted(JsonObject msg) {
      LockoutMatchData.removeActiveMatch();
      Lockout.log("Rematch accepted by everyone...");
      MinecraftUtils.notMinecraft$disconnectFromWorld();
   }

   private void handleDrawVoteStarted(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         LocalPlayer player = Minecraft.getInstance().player;
         UUID uuid = UUID.fromString(msg.get("startedByUuid").getAsString());
         Lockout lockout = LockoutMatchData.getLockout();
         LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(uuid);
         String var10000 = team.getPlayerName(uuid);
         String message = var10000 + " requested a draw. Open the board to accept it.";
         player.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.GREEN));
         player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value());
      });
   }

   private void handleDrawVoteExpired(JsonObject msg) {
      LockoutMatchData.CURRENT_MATCH.requestedDrawVote(false);
      Minecraft.getInstance().execute(() -> {
         LocalPlayer player = Minecraft.getInstance().player;
         String message = "Draw vote expired.";
         player.sendSystemMessage(Component.literal(message).withColor(ChatFormatting.RED.getColor()));
         player.playSound(SoundEvents.VILLAGER_NO);
      });
   }

   private void handleForfeited(JsonObject msg) {
      LockoutMatchData.CURRENT_MATCH.forfeited(true);
   }

   private void handleUpdateDimension(JsonObject msg) {
      if (LockoutMatchData.isInMatch()) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         UUID uuid = UUID.fromString(msg.get("uuid").getAsString());
         Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(uuid)).findFirst();
         if (!playerOptional.isEmpty()) {
            ((LockoutMatchData.LockoutMatchPlayer)playerOptional.get()).dimension(msg.get("dimension").getAsString());
         }
      }
   }

   private void handlePlayerLeft(JsonObject msg) {
      if (LockoutMatchData.isInMatch() && Minecraft.getInstance().player != null) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         UUID uuid = UUID.fromString(msg.get("uuid").getAsString());
         Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(uuid)).findFirst();
         if (!playerOptional.isEmpty()) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.format("%s left the match.", ((LockoutMatchData.LockoutMatchPlayer)playerOptional.get()).username())).withStyle(ChatFormatting.GRAY));
         }
      }
   }

   private void handlePlayerDisconnected(JsonObject msg) {
      if (LockoutMatchData.isInMatch() && Minecraft.getInstance().player != null) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         UUID uuid = UUID.fromString(msg.get("uuid").getAsString());
         Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(uuid)).findFirst();
         if (!playerOptional.isEmpty()) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.format("%s disconnected.", ((LockoutMatchData.LockoutMatchPlayer)playerOptional.get()).username())).withStyle(ChatFormatting.GRAY));
         }
      }
   }

   private void handlePlayerReconnected(JsonObject msg) {
      if (LockoutMatchData.isInMatch() && Minecraft.getInstance().player != null) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         UUID uuid = UUID.fromString(msg.get("uuid").getAsString());
         Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(uuid)).findFirst();
         if (!playerOptional.isEmpty()) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(String.format("%s reconnected.", ((LockoutMatchData.LockoutMatchPlayer)playerOptional.get()).username())).withStyle(ChatFormatting.GRAY));
         }
      }
   }

   private void handleRejoinWorld(JsonObject msg) {
      this.handleMatchFound(msg);
      MinecraftUtils.notMinecraft$execute(() -> {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         if (LockoutMatchData.isInMatch()) {
            long worldSeed = msg.get("worldSeed").getAsLong();
            matchData.gotGameData(true);
            matchData.worldSeed(worldSeed);
            matchData.startingWorldGenAt(0L);
            matchData.isRejoin(true);
            if (!Utility.isJsonNull(msg.get("rejoin")) && msg.get("rejoin").getAsBoolean()) {
               WorldOpenFlows worldOpenFlows = Minecraft.getInstance().createWorldOpenFlows();
               worldOpenFlows.openWorld(RankedData.worldName(), () -> Minecraft.getInstance().disconnect(new LockoutInformationScreen("Could not rejoin world. Try restarting your game."), false, true));
            } else {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUNDLE_DROP_CONTENTS, 0.75F, 2.0F));
               if (Minecraft.getInstance().player != null) {
                  Minecraft.getInstance().disconnectFromWorld(Component.literal("Queue found..."));
               }

               String baseName = "lockout-match";
               String uniqueName = WorldCreator.makeUniqueWorldName(baseName);
               RankedData.worldName(uniqueName);
               WorldCreator.createWorld(uniqueName, worldSeed);
            }

         }
      });
   }

   private void handleRejoinState(JsonObject msg) {
      this.handleSetGoals(msg);
      this.handleStartLockout(msg);
      LockoutMatchData.CURRENT_MATCH.isRejoin(false);
      Minecraft.getInstance().execute(() -> {
         Lockout lockout = LockoutMatchData.getLockout();
         if (lockout != null) {
            for(JsonElement completedGoalElement : msg.get("completedGoals").getAsJsonArray()) {
               JsonObject completedGoal = completedGoalElement.getAsJsonObject();
               int goalIdx = completedGoal.get("goalIndex").getAsInt();
               UUID uuid = UUID.fromString(completedGoal.get("completedByUuid").getAsString());
               Goal goal = (Goal)lockout.getBoard().getGoals().get(goalIdx);
               LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(uuid);
               goal.setCompleted(true, team);
            }
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal.getPendingChatMessage() != null && goal.isMarkedAsCompleted() && !goal.isCompleted()) {
                  goal.setMarkedAsCompleted(false);
                  lockout.completeGoal(goal, Minecraft.getInstance().getUser().getProfileId());
               }
            }
         }
      });
   }

   private void handleScreenData(JsonObject msg) {
      Utility.sendScreenDataForTwitchExtension();
   }
}
