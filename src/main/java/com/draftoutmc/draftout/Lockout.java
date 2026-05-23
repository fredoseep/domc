package com.draftoutmc.draftout;

import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.PendingChatMessage;
import com.draftoutmc.draftout.network.CompleteTaskPayload;
import com.draftoutmc.draftout.network.LockoutGoalsTeamsPayload;
import com.draftoutmc.draftout.server.LockoutServer;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import lombok.Generated;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.util.tuples.Pair;

public class Lockout {
   private static final Logger logger = LoggerFactory.getLogger(Lockout.class.getName());
   public static final Random random = new Random();
   public final Map<LockoutTeam, LinkedHashSet<EntityType<?>>> bredAnimalTypes = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<EntityType<?>>> killedHostileTypes = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<EntityType<?>>> killedRaidMobs = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<EntityType<?>>> spiedOnMobs = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<Identifier>> visitedCaveBiomes = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<Identifier>> visitedNetherBiomes = new HashMap();
   public final Map<LockoutTeam, Integer> killedUndeadMobs = new HashMap();
   public final Map<LockoutTeam, Integer> killedArthropods = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<Item>> foodTypesEaten = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<Item>> foodTypesComposted = new HashMap();
   public final Map<LockoutTeam, LinkedHashSet<Identifier>> uniqueAdvancements = new HashMap();
   public final Map<LockoutTeam, Double> damageTaken = new HashMap();
   public final Map<LockoutTeam, Double> damageDealt = new HashMap();
   public final Map<LockoutTeam, Integer> deaths = new HashMap();
   public final Map<LockoutTeam, Integer> mobsKilled = new HashMap();
   public final Map<UUID, Long> pumpkinWearTime = new HashMap();
   public final Map<UUID, Integer> distanceSprinted = new HashMap();
   public final Map<UUID, Set<Item>> uniqueCrafts = new HashMap();
   private final LockoutBoard board;
   private final List<? extends LockoutTeam> teams;
   private boolean isRunning = true;
   private long startTimeMillis = 0L;
   private long gameEndMillis = 0L;

   public Lockout(LockoutBoard board, List<? extends LockoutTeam> teams) {
      this.board = board;
      this.teams = teams;
   }

   public static void log(String message) {
      logger.info(message);
   }

   public static void error(Throwable t) {
      error("Lockout error", t);
   }

   public static void error(String message, Throwable t) {
      logger.error(message + ":\n", t);
   }

   public static boolean exists(Lockout lockout) {
      return lockout != null;
   }

   public static boolean isLockoutRunning(Lockout lockout) {
      return exists(lockout) && lockout.isRunning;
   }

   public boolean isSoloBlackout() {
      return this.teams.size() == 1 && ((LockoutTeam)this.teams.get(0)).getPlayerNames().size() == 1;
   }

   public void completeGoal(Goal goal, Player player) {
      this.completeGoal(goal, player.getUUID());
   }

   public void completeGoal(Goal goal, UUID playerId) {
      if (!goal.isCompleted()) {
         if (this.isLockoutPlayer(playerId)) {
            if (this.hasStarted()) {
               LockoutTeamServer team = (LockoutTeamServer)this.getPlayerTeam(playerId);
               String var10003 = team.getPlayerName(playerId);
               this.completeGoal(goal, team, var10003 + " completed " + goal.getGoalName() + ".");
            }
         }
      }
   }

   public void completeGoal(Goal goal, LockoutTeam team) {
      String var10003 = team.getDisplayName();
      this.completeGoal(goal, team, var10003 + " completed " + goal.getGoalName() + ".");
   }

   public void completeGoal(Goal goal, LockoutTeam team, String message) {
      if (!goal.isCompleted() && !goal.isMarkedAsCompleted()) {
         if (this.hasStarted()) {
            goal.setMarkedAsCompleted(true);
            Minecraft mc = Minecraft.getInstance();
            Runnable clientTask = () -> {
               Component pending = Component.literal(message).withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC});
               goal.setPendingChatMessage(PendingChatMessage.add(pending));
               if (mc.player != null) {
                  mc.player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 2.0F, 1.0F);
               }

            };
            if (mc.isSameThread()) {
               clientTask.run();
            } else {
               mc.execute(clientTask);
            }

            ServerConnection.getInstance().sendGoalCompleted(goal);
         }
      }
   }

   public void onGoalCompleted(Goal goal, LockoutTeam team, String message) {
      team.addPoint();
      goal.setCompleted(true, team);
      goal.setMarkedAsCompleted(true);
      boolean hadCompleted = goal.getPendingChatMessage() != null;
      if (hadCompleted) {
         goal.getPendingChatMessage().remove();
      }

      for(LockoutTeam lockoutTeam : this.teams) {
         if (lockoutTeam instanceof LockoutTeamServer lockoutTeamServer) {
            if (Objects.equals(lockoutTeamServer, team)) {
               lockoutTeamServer.sendMessage((Component)Component.empty().append(Component.literal(message).withStyle(ChatFormatting.GREEN)));
            } else {
               lockoutTeamServer.sendMessage((Component)Component.empty().append(Component.literal(message).withStyle(ChatFormatting.RED)).append(Component.literal(hadCompleted ? " (adjusted for ping)" : "").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC})));
            }
         }
      }

      this.sendGoalCompletedPacket(goal, team);
   }

   public void complete1v1Goal(Goal goal, Player player, boolean isWinner, String message) {
      this.complete1v1Goal(goal, player.getUUID(), isWinner, message);
   }

   public void complete1v1Goal(Goal goal, UUID playerId, boolean isWinner, String message) {
      if (!goal.isCompleted()) {
         if (this.isLockoutPlayer(playerId)) {
            if (this.hasStarted()) {
               LockoutTeamServer team = (LockoutTeamServer)this.getPlayerTeam(playerId);
               this.complete1v1Goal(goal, (LockoutTeam)team, isWinner, message);
            }
         }
      }
   }

   public void complete1v1Goal(Goal goal, LockoutTeam team, boolean isWinner, String message) {
      if (!goal.isCompleted()) {
         if (this.hasStarted()) {
            LockoutTeamServer opponentTeam = (LockoutTeamServer)this.getOpponentTeam(team);
            LockoutTeamServer winnerTeam = isWinner ? (LockoutTeamServer)team : opponentTeam;
            LockoutTeamServer loserTeam = isWinner ? opponentTeam : (LockoutTeamServer)team;
            goal.setCompleted(true, winnerTeam);
            winnerTeam.addPoint();
            String var10001 = String.valueOf(ChatFormatting.GREEN);
            winnerTeam.sendMessage(var10001 + message);
            var10001 = String.valueOf(ChatFormatting.RED);
            loserTeam.sendMessage(var10001 + message);
            this.sendGoalCompletedPacket(goal, winnerTeam);
            this.evaluateWinnerAndEndGame(winnerTeam);
         }
      }
   }

   public void updateGoalCompletion(Goal goal, UUID playerId) {
      if (goal.isCompleted()) {
         this.clearGoalCompletion(goal, false);
      }

      this.completeGoal(goal, playerId);
   }

   public void clearGoalCompletion(Goal goal, boolean sendPacket) {
      if (goal.isCompleted()) {
         goal.getCompletedTeam().takePoint();
         goal.setCompleted(false, (LockoutTeam)null);
         if (sendPacket) {
            CompleteTaskPayload payload = new CompleteTaskPayload(goal.getId(), -1);

            for(ServerPlayer serverPlayer : LockoutServer.server.getPlayerList().getPlayers()) {
               ServerPlayNetworking.send(serverPlayer, payload);
            }
         }

      }
   }

   private void sendGoalCompletedPacket(Goal goal, LockoutTeam team) {
      CompleteTaskPayload payload = new CompleteTaskPayload(goal.getId(), this.teams.indexOf(team));

      for(ServerPlayer serverPlayer : LockoutServer.server.getPlayerList().getPlayers()) {
         ServerPlayNetworking.send(serverPlayer, payload);
      }

   }

   private void evaluateWinnerAndEndGame(LockoutTeam team) {
      PlayerList playerManager = LockoutServer.server.getPlayerList();
      List<LockoutTeam> winners = new ArrayList();
      if (this.isWinner(team)) {
         playerManager.broadcastSystemMessage(Component.literal(team.getDisplayName() + " wins."), false);
         winners.add(team);
         this.setRunning(false);
      } else if (this.getRemainingGoals() == 0 && this.teams.size() > 1) {
         int maxCompleted = this.getMostCompletedGoals();
         List<? extends LockoutTeam> winnerTeams = this.teams.stream().filter((t) -> t.getPoints() == maxCompleted).toList();
         winners.addAll(winnerTeams);
         playerManager.broadcastSystemMessage(Component.literal("It's a tie! " + getWinnerTeamsString(winnerTeams) + " win."), false);
         this.setRunning(false);
      }

   }

   public int getMostCompletedGoals() {
      return ((LockoutTeam)this.teams.stream().max(Comparator.comparingInt(LockoutTeam::getPoints)).get()).getPoints();
   }

   public boolean hasStarted() {
      return System.currentTimeMillis() > this.startTimeMillis;
   }

   public boolean isLockoutPlayer(Player player) {
      return this.isLockoutPlayer(player.getUUID());
   }

   public boolean isLockoutPlayer(UUID playerId) {
      for(LockoutTeam team : this.teams) {
         if (((LockoutTeamServer)team).getPlayers().contains(playerId)) {
            return true;
         }
      }

      return false;
   }

   public LockoutTeam getPlayerTeam(UUID playerId) {
      for(LockoutTeam team : this.teams) {
         if (((LockoutTeamServer)team).getPlayers().contains(playerId)) {
            return team;
         }
      }

      return null;
   }

   public LockoutTeam getOpponentTeam(UUID playerId) {
      for(LockoutTeam team : this.teams) {
         if (!((LockoutTeamServer)team).getPlayers().contains(playerId)) {
            return team;
         }
      }

      return null;
   }

   public LockoutTeam getOpponentTeam(LockoutTeam team) {
      for(LockoutTeam t : this.teams) {
         if (!Objects.equals(t, team)) {
            return t;
         }
      }

      return null;
   }

   public boolean isWinner(LockoutTeam team) {
      if (this.teams.size() == 1) {
         return this.getRemainingGoals() == 0;
      } else {
         for(LockoutTeam t : this.teams) {
            if (team != t && t.getPoints() + this.getRemainingGoals() >= team.getPoints()) {
               return false;
            }
         }

         return true;
      }
   }

   public int getRemainingGoals() {
      return (int)this.board.getGoals().stream().filter((goal) -> !goal.isCompleted()).count();
   }

   private static String getWinnerTeamsString(List<? extends LockoutTeam> teams) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < teams.size(); ++i) {
         if (i > 0) {
            if (i + 1 == teams.size()) {
               sb.append(" and ");
            } else {
               sb.append(", ");
            }
         }

         LockoutTeam team = (LockoutTeam)teams.get(i);
         sb.append(team.getDisplayName());
      }

      return sb.toString();
   }

   public LockoutGoalsTeamsPayload getTeamsGoalsPacket() {

      // 1. 还原队伍列表 (直接传入或复制集合，去除冗余的 Stream)
      // 如果 this.teams 本身就是 List 并且允许直接传递，可以直接用 this.teams
      List<LockoutTeam> teamList = new ArrayList<>(this.teams);

      // 2. 还原并格式化目标列表 (补全泛型 <>)
      var goalList = this.board.getGoals().stream()
              .map(goal -> new Pair<>(
                      new Pair<>(goal.getId(), goal.getData()),
                      this.teams.indexOf(goal.getCompletedTeam())
              ))
              .toList();

      // 3. 构建并返回 Payload
      return new LockoutGoalsTeamsPayload(teamList, goalList, this.isRunning);
   }

   public long getTimer() {
      return (this.isRunning() ? System.currentTimeMillis() : this.getGameEndMillis()) - this.getStartTimeMillis();
   }

   @Generated
   public LockoutBoard getBoard() {
      return this.board;
   }

   @Generated
   public List<? extends LockoutTeam> getTeams() {
      return this.teams;
   }

   @Generated
   public boolean isRunning() {
      return this.isRunning;
   }

   @Generated
   public void setRunning(boolean isRunning) {
      this.isRunning = isRunning;
   }

   @Generated
   public void setStartTimeMillis(long startTimeMillis) {
      this.startTimeMillis = startTimeMillis;
   }

   @Generated
   public long getStartTimeMillis() {
      return this.startTimeMillis;
   }

   @Generated
   public void setGameEndMillis(long gameEndMillis) {
      this.gameEndMillis = gameEndMillis;
   }

   @Generated
   public long getGameEndMillis() {
      return this.gameEndMillis;
   }
}
