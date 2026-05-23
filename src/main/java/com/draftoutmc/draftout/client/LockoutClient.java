package com.draftoutmc.draftout.client;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.LockoutRunnable;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.export.GoalIconExportRunner;
import com.draftoutmc.draftout.client.gui.BoardScreen;
import com.draftoutmc.draftout.client.gui.ranked.elements.ItemGridClientTooltipComponent;
import com.draftoutmc.draftout.client.gui.ranked.elements.ItemGridTooltip;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutWaitingScreen;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.RankedUtils;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.network.CompleteTaskPayload;
import com.draftoutmc.draftout.network.EndLockoutPayload;
import com.draftoutmc.draftout.network.LockoutGoalsTeamsPayload;
import com.draftoutmc.draftout.network.StartLockoutPayload;
import com.draftoutmc.draftout.network.UpdateTooltipPayload;
import com.draftoutmc.draftout.server.LockoutServer;
import com.draftoutmc.draftout.websocket.ServerConnection;
import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.HashSet;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import oshi.util.tuples.Pair;

public class LockoutClient implements ClientModInitializer {
   public static boolean amIPlayingLockout = false;
   public static KeyMapping OPEN_BOARD_HOTKEY;
   public static int RENDER_TICK = 0;

   public void onInitializeClient() {
      GoalIconExportRunner.initializeIfRequested();
      ClientPlayNetworking.registerGlobalReceiver(LockoutGoalsTeamsPayload.ID, (payload, context) -> {
         List<LockoutTeam> teams = payload.teams();
         amIPlayingLockout = teams.stream().map(LockoutTeam::getPlayerNames).anyMatch((players) -> players.stream().anyMatch((player) -> player.equals(Minecraft.getInstance().getUser().getName())));
         int[] completedByTeam = payload.goals().stream().mapToInt(Pair::getB).toArray();
         Lockout lockout = LockoutMatchData.getLockout();
         lockout.setRunning(payload.isRunning());
         List<Goal> goalList = lockout.getBoard().getGoals();

         for(int i = 0; i < goalList.size(); ++i) {
            if (completedByTeam[i] != -1) {
               LockoutTeam team = (LockoutTeam)lockout.getTeams().get(completedByTeam[i]);
               ((Goal)goalList.get(i)).setCompleted(true, team);
               team.addPoint();
            }
         }

         Minecraft client = context.client();
         client.execute(() -> {
            if (client.player != null) {
               client.setScreen(new BoardScreen());
            }

         });
      });
      ClientPlayNetworking.registerGlobalReceiver(UpdateTooltipPayload.ID, (payload, context) -> context.client().execute(() -> TooltipCache.receive(payload)));
      ClientPlayNetworking.registerGlobalReceiver(StartLockoutPayload.ID, (payload, context) -> context.client().execute(() -> {
            if (Minecraft.getInstance().screen != null) {
               Minecraft.getInstance().screen.onClose();
            }

         }));
      ClientPlayNetworking.registerGlobalReceiver(CompleteTaskPayload.ID, (payload, context) -> {
         Minecraft client = context.client();
         client.execute(() -> {
            Lockout lockout = LockoutMatchData.getLockout();
            Goal goal = (Goal)lockout.getBoard().getGoals().stream().filter((g) -> g.getId().equals(payload.goal())).findFirst().get();
            if (goal.isCompleted() || payload.teamIndex() == -1) {
               lockout.clearGoalCompletion(goal, false);
            }

            if (payload.teamIndex() != -1) {
               LockoutTeam team = (LockoutTeam)lockout.getTeams().get(payload.teamIndex());
               team.addPoint();
               goal.setCompleted(true, (LockoutTeam)lockout.getTeams().get(payload.teamIndex()));
               if (client.player != null && amIPlayingLockout && !team.getPlayerNames().contains(client.player.getName().getString())) {
                  client.player.playSound(SoundEvents.GUARDIAN_DEATH, 2.0F, 1.0F);
               }
            }

         });
      });
      ClientPlayNetworking.registerGlobalReceiver(EndLockoutPayload.ID, (payload, context) -> {
         Lockout lockout = LockoutMatchData.getLockout();
         lockout.setRunning(false);
         Minecraft client = context.client();
         client.execute(() -> {
            if (client.player != null) {
               boolean didIWin = false;

               for(int winner : payload.winners()) {
                  LockoutTeam team = (LockoutTeam)lockout.getTeams().get(winner);
                  if (team.getPlayerNames().contains(client.player.getName().getString())) {
                     didIWin = true;
                     break;
                  }
               }

               if (didIWin) {
                  client.player.playSound(SoundEvents.PILLAGER_CELEBRATE, 2.0F, 1.0F);
               } else {
                  client.player.playSound(SoundEvents.WARDEN_DEATH, 2.0F, 1.0F);
               }
            }

         });
      });
      OPEN_BOARD_HOTKEY = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.lockout.open_board", Type.KEYSYM, 66, new KeyMapping.Category(Identifier.fromNamespaceAndPath("lockout", "keybinds"))));
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         GoalIconExportRunner.ensureBuiltInItemTagsBound(client);
         GoalIconExportRunner.ensureBuiltInItemComponentsBound(client);
         Lockout lockout = LockoutMatchData.getLockout();
         ++RENDER_TICK;

         HashSet<LockoutRunnable> hashSet = new HashSet(LockoutServer.RUNNABLES.keySet());

         for(LockoutRunnable runnable : hashSet) {
            long scheduledTime = (Long)LockoutServer.RUNNABLES.get(runnable);
            if (System.currentTimeMillis() >= scheduledTime) {
               runnable.run();
               LockoutServer.RUNNABLES.remove(runnable);
            }
         }

         boolean wasPressed;
         for(wasPressed = false; OPEN_BOARD_HOTKEY.consumeClick(); wasPressed = true) {
         }

         if (wasPressed) {
            if (client.player == null) {
               return;
            }

            if (RankedUtils.isInDraftPhase()) {
               if (client.screen == null) {
                  Minecraft.getInstance().setScreen(LockoutMatchData.CURRENT_MATCH.draftScreen());
               }
            } else if (Lockout.exists(lockout) && client.screen == null) {
               client.setScreen(new BoardScreen());
            }
         }

      });
      LockoutConfig.load();
      ClientPlayConnectionEvents.JOIN.register((ClientPlayConnectionEvents.Join)(handler, sender, client) -> {
         LockoutConfig.load();
         if (LockoutMatchData.isInMatch() && !Lockout.isLockoutRunning(LockoutMatchData.getLockout())) {
            if (!LockoutMatchData.CURRENT_MATCH.isRejoin()) {
               client.setScreen(new LockoutWaitingScreen("Waiting for players..."));
            }

            ServerConnection.getInstance().sendWorldJoined();
         }

      });
      ClientPlayConnectionEvents.DISCONNECT.register((ClientPlayConnectionEvents.Disconnect)(handler, client) -> LockoutMatchData.setLockout((Lockout)null));
      ClientTooltipComponentCallback.EVENT.register((ClientTooltipComponentCallback)(data) -> {
         if (data instanceof ItemGridTooltip grid) {
            return new ItemGridClientTooltipComponent(grid);
         } else {
            return null;
         }
      });

      try {
         Utility.deleteOldVersions();
      } catch (Exception e) {
         Lockout.error("Failed to delete old jars", e);
      }

   }
}
