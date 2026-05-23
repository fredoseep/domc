package com.draftoutmc.draftout.websocket;

import com.draftoutmc.draftout.LockoutRunnable;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutDraftScreen;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.server.LockoutServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import oshi.util.tuples.Pair;

public class DraftoutMessageHandler {
   private ServerConnection sc;

   public DraftoutMessageHandler(ServerConnection sc) {
      this.sc = sc;
   }

   public void handle(String type, JsonObject msg) {
      switch (type) {
         case "start_scout" -> this.handleStartScout(msg);
         case "scout_rejoin" -> this.handleRejoinScout(msg);
         case "start_draft" -> this.handleStartDraft(msg);
         case "draft_rejoin" -> this.handleRejoinDraft(msg);
         case "update" -> this.handleUpdateDraft(msg);
      }

   }

   private void handleStartScout(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         LockoutServer.resetPlayers();
         Minecraft.getInstance().setScreen((Screen)null);
         long currentTime = System.currentTimeMillis();
         int scoutingTimeMs = msg.get("scoutingTimeMs").getAsInt();

         for(int i = 1; i <= scoutingTimeMs / 1000; ++i) {
            int finalI = i;
            LockoutRunnable dataTask = () -> {
               if (finalI <= 3) {
                  Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), 1.0F, 1.0F));
               }

               if (Minecraft.getInstance().player != null && LockoutMatchData.isInMatch()) {
                  Minecraft.getInstance().player.sendOverlayMessage(Component.literal("Draft begins in " + finalI + "s...").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
               }

            };
            dataTask.runTaskAt(currentTime + (long)scoutingTimeMs - ((long)i * 1000L + (long)(scoutingTimeMs % 1000)));
         }
         LockoutRunnable matchTask = () -> {
            Minecraft.getInstance().player.sendOverlayMessage(Component.empty());
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), 2.0F, 1.0F));
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI((SoundEvent)SoundEvents.NOTE_BLOCK_PLING.value(), 2.0F, 1.0F));
            if (Minecraft.getInstance().player != null && LockoutMatchData.isInMatch()) {
               LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
               matchData.draftBegun(true);
               Minecraft.getInstance().setScreen(matchData.draftScreen());
            }

         };
         matchTask.runTaskAt(currentTime + (long)scoutingTimeMs);
      });
   }

   private void handleRejoinScout(JsonObject msg) {
      this.sc.onGameMessage("set_goals", msg);
      this.handleStartScout(msg);
   }

   private void handleStartDraft(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         if (LockoutMatchData.isInMatch()) {
            LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
            EditBox chat = null;
            Screen patt0$temp = Minecraft.getInstance().screen;
            if (patt0$temp instanceof ChatScreen) {
               ChatScreen chatScreen = (ChatScreen)patt0$temp;
               chat = chatScreen.input;
            }

            LockoutDraftScreen screen = new LockoutDraftScreen(matchData.players(), matchData.goals, chat);
            matchData.draftBegun(false);
            matchData.draftScreen(screen);
            matchData.draftIdx(-1);
            List<Pair<String, String>> goals = new ArrayList();

            for(int i = 0; i < 25; ++i) {
               goals.add(new Pair("null", "null"));
            }

            matchData.pickedBoard(new LockoutBoard(goals));
            Minecraft.getInstance().setScreen(screen);
         }
      });
   }

   private void handleRejoinDraft(JsonObject msg) {
      this.sc.onGameMessage("set_goals", msg);
      Minecraft.getInstance().execute(() -> {
         if (LockoutMatchData.isInMatch()) {
            LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
            EditBox chat = null;
            Screen patt0$temp = Minecraft.getInstance().screen;
            if (patt0$temp instanceof ChatScreen) {
               ChatScreen chatScreen = (ChatScreen)patt0$temp;
               chat = chatScreen.input;
            }

            LockoutDraftScreen screen = new LockoutDraftScreen(matchData.players(), matchData.goals, chat);
            matchData.draftBegun(false);
            matchData.draftScreen(screen);
            Minecraft.getInstance().setScreen(screen);
         }
      });
      this.handleUpdateDraft(msg);
   }

   private void handleUpdateDraft(JsonObject msg) {
      Minecraft.getInstance().execute(() -> {
         JsonArray picksArray = msg.get("picks").getAsJsonArray();
         UUID uuid = msg.get("picksNext").isJsonNull() ? null : UUID.fromString(msg.get("picksNext").getAsString());
         int draftIdx = msg.get("draftIdx").getAsInt();
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         List<Pair<String, String>> goals = new ArrayList();

         for(int i = 0; i < 25; ++i) {
            if (i >= picksArray.size()) {
               goals.add(new Pair("null", "null"));
            } else {
               JsonElement goal = picksArray.get(i);
               String goalId = goal.getAsJsonObject().get("id").getAsString();
               String goalData = goal.getAsJsonObject().get("data").getAsString();
               goals.add(new Pair(goalId, goalData));
            }
         }

         matchData.pickedBoard(new LockoutBoard(goals));
         matchData.picksNext(uuid);
         matchData.draftIdx(draftIdx);
         matchData.picked(false);
         matchData.pickTimeMs((long)msg.get("pickTime").getAsInt());
         matchData.pickEndsAt(System.currentTimeMillis() + (long)msg.get("pickTime").getAsInt());
         if (uuid != null && uuid.equals(Minecraft.getInstance().getUser().getProfileId()) && !(Minecraft.getInstance().screen instanceof LockoutDraftScreen)) {
            Minecraft.getInstance().setScreen(matchData.draftScreen());
         }

         if (matchData.draftIdx() > 0) {
            if (matchData.draftIdx() <= 25) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.COPPER_BULB_PLACE, 1.0F + (float)matchData.draftIdx() * 0.02F));
            }

            if (matchData.draftIdx() >= 25) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BEACON_POWER_SELECT, 1.0F));
            }
         }

      });
   }
}
