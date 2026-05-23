package com.draftoutmc.draftout.server;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.LockoutRunnable;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.GoalRegistry;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.mixin.server.StatsCounterAccessor;
import com.draftoutmc.draftout.network.StartLockoutPayload;
import com.draftoutmc.draftout.server.handlers.AfterDeathEventHandler;
import com.draftoutmc.draftout.server.handlers.AfterPlayerChangeWorldEventHandler;
import com.draftoutmc.draftout.server.handlers.AfterRespawnEventHandler;
import com.draftoutmc.draftout.server.handlers.EndServerTickEventHandler;
import com.draftoutmc.draftout.server.handlers.PlayerChangeLevelEventHandler;
import com.draftoutmc.draftout.server.handlers.PlayerJoinEventHandler;
import com.draftoutmc.draftout.server.handlers.ServerStartedEventHandler;
import com.draftoutmc.draftout.server.handlers.UseBlockEventHandler;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.AdvancementCommands.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.level.GameType;
import oshi.util.tuples.Pair;

public class LockoutServer {
   public static MinecraftServer server;
   public static final Map<LockoutRunnable, Long> RUNNABLES = new HashMap();
   public static LockoutBoard board = null;
   private static boolean isInitialized = false;

   public static void initializeServer() {
      RUNNABLES.clear();
      LockoutConfig.load();
      if (!isInitialized) {
         isInitialized = true;
         ServerPlayerEvents.AFTER_RESPAWN.register(new AfterRespawnEventHandler());
         ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register(new AfterPlayerChangeWorldEventHandler());
         ServerPlayConnectionEvents.JOIN.register(new PlayerJoinEventHandler());
         ServerTickEvents.END_SERVER_TICK.register(new EndServerTickEventHandler());
         ServerLivingEntityEvents.AFTER_DEATH.register(new AfterDeathEventHandler());
         UseBlockCallback.EVENT.register(new UseBlockEventHandler());
         ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedEventHandler());
         ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register(new PlayerChangeLevelEventHandler());
      }
   }

   public static void resetPlayers() {
      PlayerList playerManager = server.getPlayerList();

      for(ServerPlayer serverPlayer : playerManager.getPlayers()) {
         serverPlayer.getInventory().clearContent();
         serverPlayer.setHealth(serverPlayer.getMaxHealth());
         serverPlayer.removeAllEffects();
         serverPlayer.getFoodData().setSaturation(5.0F);
         serverPlayer.getFoodData().setFoodLevel(20);
         serverPlayer.getFoodData().exhaustionLevel = 0.0F;
         serverPlayer.setExperienceLevels(0);
         serverPlayer.setExperiencePoints(0);
         serverPlayer.setSharedFlagOnFire(false);
         resetAllStats(serverPlayer);
         Action.REVOKE.perform(serverPlayer, server.getAdvancements().getAllAdvancements(), false);
         serverPlayer.setGameMode(GameType.ADVENTURE);
      }

   }

   public static void startLockout(List<LockoutTeamServer> teams, List<Pair<String, String>> board, long gameTime) {
      if (LockoutMatchData.isInMatch()) {
         RUNNABLES.clear();
         List<String> invalidGoals = new ArrayList();

         for(Pair<String, String> entry : board) {
            if (!GoalRegistry.INSTANCE.isGoalValid((String)entry.getA(), (String)entry.getB())) {
               String var10001 = (String)entry.getA();
               invalidGoals.add(" - '" + var10001 + "'" + ("null".equals(entry.getB()) ? "" : " with data: '" + (String)entry.getB() + "'"));
            }
         }

         if (!invalidGoals.isEmpty()) {
            Lockout.log("Invalid board. Could not create goals:\n" + String.join("\n", invalidGoals));
         }

         LockoutServer.board = new LockoutBoard(board);
         PlayerList playerManager = server.getPlayerList();
         List<ServerPlayer> allServerPlayers = playerManager.getPlayers();
         List<UUID> allLockoutPlayers = teams.stream().flatMap((teamx) -> teamx.getPlayers().stream()).toList();
         resetPlayers();
         ServerLevel world = server.createCommandSourceStack().getLevel();

         for(Goal goal : LockoutServer.board.getGoals()) {
            goal.setCompleted(false, (LockoutTeam)null);
         }

         LockoutBoard lockoutBoard = LockoutServer.board;
         Lockout lockout = new Lockout(lockoutBoard, teams);
         LockoutMatchData.setLockout(lockout);
         lockout.setStartTimeMillis(System.currentTimeMillis() - gameTime);
         List<Goal> goals = new ArrayList<>(lockout.getBoard().getGoals());

         for (Goal goal : goals) {
            for(LockoutTeam team : lockout.getTeams()) {
               ((LockoutTeamServer)team).sendTooltipUpdate((Goal & HasTooltipInfo)goal);
            }
         }

         for(ServerPlayer player : allServerPlayers) {
            ServerPlayNetworking.send(player, lockout.getTeamsGoalsPacket());
         }

         ResourceKey<WorldClock> overworldClock = ResourceKey.create(Registries.WORLD_CLOCK, Identifier.withDefaultNamespace("overworld"));
         Holder.Reference<WorldClock> clock = world.registryAccess().getOrThrow(overworldClock);
         world.clockManager().setTotalTicks(clock, 0L);

         for(int i = 3; i >= 0; --i) {
            // 修复 1：创建一个事实上的 final 变量供 Lambda 使用
            final int currentI = i;

            if (currentI > 0) {
               // 修复 2：还原正确的方法调用格式 runTaskAt(Runnable, long)
               LockoutRunnable uiTask = () -> {
                  if (!lockout.hasStarted()) {
                     Minecraft.getInstance().gui.setTimes(5, 10, 5);
                     Minecraft.getInstance().gui.setTitle(Component.literal(String.valueOf(currentI)).withStyle(ChatFormatting.BOLD));
                     Minecraft.getInstance().gui.setSubtitle(Component.empty());
                     Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), 1.0F, 1.0F));
                  }

                  if (currentI == 3) {
                     server.tickRateManager().setFrozen(false);
                     server.getPlayerList().broadcastSystemMessage(Component.literal("Game ticks have been unfrozen").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY), true);
                  }
               };
               uiTask.runTaskAt(lockout.getStartTimeMillis() - (long)currentI * 1000L);

            } else {
               LockoutRunnable playerTask = () -> {
                  for(ServerPlayer player : allServerPlayers) {
                     if (player != null) {
                        ServerPlayNetworking.send(player, StartLockoutPayload.INSTANCE);
                        if (allLockoutPlayers.contains(player.getUUID())) {
                           player.setGameMode(GameType.SURVIVAL);
                        }
                     }
                  }

                  Minecraft.getInstance().gui.setTimes(5, 10, 5);
                  Minecraft.getInstance().gui.setTitle(Component.empty());
                  Minecraft.getInstance().gui.setSubtitle(Component.literal("Lockout has begun."));
                  Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), 2.0F, 1.0F));
                  Minecraft.getInstance().setScreen(null);
               };
               playerTask.runTaskAt(lockout.getStartTimeMillis());

            }
         }

      }
   }

   public static int parseIntoTeams(List<LockoutTeamServer> teams, List<LockoutMatchData.LockoutMatchPlayer> players) {
      for(LockoutMatchData.LockoutMatchPlayer player : players) {
         String name = player.username();
         UUID uuid = player.uuid();
         teams.add(new LockoutTeamServer(Collections.singletonMap(name, uuid), player.lockoutTeamServer().getColor()));
      }

      return 1;
   }

   public static int giveGoal(CommandContext<CommandSourceStack> context) {
      try {
         Lockout lockout = LockoutMatchData.getLockout();
         if (!Lockout.isLockoutRunning(lockout)) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("There's no active lockout match."));
            return 0;
         } else {
            int idx = (Integer)context.getArgument("goal number", Integer.class);

            Collection<NameAndId> gps;
            try {
               gps = GameProfileArgument.getGameProfiles(context, "player name");
            } catch (CommandSyntaxException var6) {
               ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Invalid target."));
               return 0;
            }

            if (gps.size() != 1) {
               ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Invalid number of targets."));
               return 0;
            } else {
               NameAndId gp = (NameAndId)gps.stream().findFirst().get();
               if (!lockout.isLockoutPlayer(gp.id())) {
                  ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Player " + gp.name() + " is not playing Lockout."));
                  return 0;
               } else if (idx > lockout.getBoard().getGoals().size()) {
                  ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Goal number does not exist on the board."));
                  return 0;
               } else {
                  Goal goal = (Goal)lockout.getBoard().getGoals().get(idx - 1);
                  CommandSourceStack var10000 = (CommandSourceStack)context.getSource();
                  String var10001 = gp.name();
                  var10000.sendSystemMessage(Component.nullToEmpty("Gave " + var10001 + " goal \"" + goal.getGoalName() + "\"."));
                  lockout.updateGoalCompletion(goal, gp.id());
                  return 1;
               }
            }
         }
      } catch (RuntimeException e) {
         Lockout.error(e);
         return 0;
      }
   }

   public static int setBoardSize(CommandContext<CommandSourceStack> context) {
      int size = (Integer)context.getArgument("board size", Integer.class);
      ((CommandSourceStack)context.getSource()).sendSystemMessage(Component.nullToEmpty("Updated board size to " + size + "."));
      return 1;
   }

   public static void resetAllStats(ServerPlayer serverPlayer) {
      try {
         ServerStatsCounter counter = serverPlayer.getStats();
         counter.markAllDirty();
         ((StatsCounterAccessor)counter).getStatsMap().clear();
         counter.sendStats(serverPlayer);
      } catch (Exception e) {
         Lockout.error(e);
      }

   }
}
