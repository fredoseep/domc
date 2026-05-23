package com.draftoutmc.draftout.websocket;

import com.draftoutmc.draftout.Initializer;
import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.gui.ranked.screen.ConnectTwitchScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.LeaderboardScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutInformationScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutMainScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutWaitingScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.ModUpdateScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.PlayerMatchesScreen;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.MinecraftUtils;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.MatchHistoryData;
import com.draftoutmc.draftout.match.data.PlayerProfile;
import com.draftoutmc.draftout.match.data.RankedData;
import com.draftoutmc.draftout.server.LockoutServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ServerConnection extends WebSocketClient {
   private static final Gson GSON = new Gson();
   private static ServerConnection INSTANCE;
   private static URI uri;
   private String serverId;
   public boolean connected;
   public MatchType queueType;
   public long queueStartTimeMs;
   public Screen previousScreen;
   private final GameMessageHandler gameMessageHandler = new GameMessageHandler(this);
   private final DraftoutMessageHandler draftoutMessageHandler = new DraftoutMessageHandler(this);
   private boolean playDisconnectSound = true;

   private ServerConnection() {
      super(uri);
      this.setConnectionLostTimeout(25);
      Lockout.log("Setup server connection with uri: " + String.valueOf(super.uri));
   }

   public static void setType(Type type) {
      if (INSTANCE != null && INSTANCE.connected) {
         INSTANCE.close();
         INSTANCE = null;
      }

      uri = URI.create(type.path);
   }

   public static ServerConnection getInstance() {
      if (INSTANCE == null || INSTANCE.isClosed()) {
         INSTANCE = new ServerConnection();
      }

      return INSTANCE;
   }

   public static boolean connected() {
      return INSTANCE != null && INSTANCE.connected;
   }

   public void connectWithTimeout() {
      (new Thread(() -> {
         try {
            Lockout.log("Connecting");
            if (!this.connectBlocking(5L, TimeUnit.SECONDS)) {
               this.close();
               Lockout.log("Could not connect");
            } else {
               Lockout.log("Connected");
            }
         } catch (InterruptedException var2) {
            this.close();
         }

      })).start();
   }

   public void onOpen(ServerHandshake handshake) {
      this.sendGetServerId();
   }

   public void onClose(int code, String reason, boolean remote) {
      this.connected = false;
      this.queueType = null;
      this.queueStartTimeMs = 0L;
      LockoutServer.RUNNABLES.clear();
      LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
      LockoutMatchData.removeActiveMatch();
      Minecraft.getInstance().execute(() -> {
         if (this.playDisconnectSound) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ANVIL_DESTROY, 1.0F));
         }

         if (matchData != null && Minecraft.getInstance().level != null) {
            Minecraft.getInstance().disconnect(new LockoutInformationScreen("Disconnected from server.\nTry reconnecting."), false, true);
         }

         if (Minecraft.getInstance().screen instanceof LockoutWaitingScreen || Minecraft.getInstance().screen instanceof LockoutMainScreen || Minecraft.getInstance().screen instanceof LeaderboardScreen || Minecraft.getInstance().screen instanceof PlayerMatchesScreen) {
            Minecraft.getInstance().setScreen(new TitleScreen());
         }

      });
   }

   public boolean queued() {
      return this.queueType != null;
   }

   public void onMessage(String raw) {
      try {
         JsonObject msg = (JsonObject)GSON.fromJson(raw, JsonObject.class);
         if (msg.get("type") == null || msg.get("type").isJsonNull() || msg.get("type").getAsString().isBlank()) {
            Lockout.log("Received invalid message from server");
            return;
         }

         String[] type = msg.get("type").getAsString().split("\\.");
         if (!type[0].equals("ping")) {
            Lockout.log("Received message: " + Arrays.toString(type));
         }

         switch (type[0]) {
            case "auth" -> this.onAuthMessage(type[1], msg);
            case "game" -> this.onGameMessage(type[1], msg);
            case "draftout" -> this.onDraftoutMessage(type[1], msg);
            case "data" -> this.onDataMessage(type[1], msg);
            case "ping" -> this.sendPong();
            case "dev" -> this.onDevMessage(type[1], msg);
            default -> Lockout.log("Unknown message type from server: " + type[0]);
         }
      } catch (Exception e) {
         Lockout.error("Unhandled websocket error", e);
      }

   }

   public void onAuthMessage(String type, JsonObject msg) {
      switch (type) {
         case "id":
            this.serverId = msg.get("id").getAsString();
            this.sendConnectionRequest();
            break;
         case "connected":
            boolean connected = msg.get("connected").getAsBoolean();
            if (!connected) {
               String reason = msg.get("reason").getAsString();
               if (!Utility.isJsonNull(msg.get("update")) && msg.get("update").getAsBoolean()) {
                  this.playDisconnectSound = false;
                  String downloadURL = msg.get("downloadURL").getAsString();
                  Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new ModUpdateScreen(reason, downloadURL)));
                  return;
               }

               Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LockoutInformationScreen(reason)));
               return;
            }

            JsonObject connections = msg.get("connections") != null && !msg.get("connections").isJsonNull() ? msg.get("connections").getAsJsonObject() : null;
            if (connections != null) {
               String twitchDisplayName = connections.get("twitch") != null && !connections.get("twitch").isJsonNull() ? connections.get("twitch").getAsString() : null;
               RankedData.linkedTwitchUsername(twitchDisplayName);
            }

            Minecraft.getInstance().execute(() -> RankedData.myProfile(PlayerProfile.parse(msg.get("profile").getAsJsonObject())));
            this.onConnected();
            break;
         case "twitch":
            if (LockoutMatchData.isInMatchWorld()) {
               return;
            }

            Minecraft.getInstance().execute(() -> {
               String url = msg.get("url").getAsString();
               Minecraft.getInstance().setScreen(new ConnectTwitchScreen(url));
            });
            break;
         case "twitch_linked":
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WANDERING_TRADER_YES, 1.0F));
            if (LockoutMatchData.isInMatchWorld()) {
               return;
            }

            RankedData.linkedTwitchUsername(msg.get("display_name").getAsString());
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LockoutMainScreen()));
            break;
         case "twitch_disconnected":
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WANDERING_TRADER_NO, 1.0F));
            if (LockoutMatchData.isInMatchWorld()) {
               return;
            }

            RankedData.linkedTwitchUsername((String)null);
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LockoutMainScreen()));
      }

   }

   public void onDataMessage(String type, JsonObject msg) {
      switch (type) {
         case "leaderboard" -> Minecraft.getInstance().execute(() -> {
   if (!LockoutMatchData.isInMatchWorld()) {
      JsonArray lb = msg.get("leaderboard").getAsJsonArray();
      List<LockoutMatchData.LockoutMatchPlayer> players = new ArrayList();

      for(JsonElement player : lb) {
         players.add(LockoutMatchData.LockoutMatchPlayer.fromJsonObject((JsonObject)player));
      }

      Minecraft.getInstance().setScreen(new LeaderboardScreen(players));
   }
});
         case "profile" -> Minecraft.getInstance().execute(() -> {
   if (!LockoutMatchData.isInMatchWorld()) {
      if (!Utility.isJsonNull(msg.get("error"))) {
         Minecraft.getInstance().setScreen(new LockoutInformationScreen("Could not find profile:\n" + msg.get("error").getAsString(), Component.literal("Error"), new LockoutMainScreen(), "Back to Main Menu"));
      } else {
         PlayerProfile profile = PlayerProfile.parse(msg.get("profile").getAsJsonObject());
         this.onMatchesReceived(msg.get("matches").getAsJsonObject(), profile);
      }
   }
});
         case "matches" -> Minecraft.getInstance().execute(() -> this.onMatchesReceived(msg, (PlayerProfile)null));
      }

   }

   private void onMatchesReceived(JsonObject matchesObject, PlayerProfile profile) {
      if (!LockoutMatchData.isInMatchWorld()) {
         try {
            MatchHistoryData.MatchPage matchPage = MatchHistoryData.parse(matchesObject);
            if (profile != null) {
               Minecraft.getInstance().setScreen(new PlayerMatchesScreen(matchPage, profile));
            } else {
               Screen var5 = Minecraft.getInstance().screen;
               if (var5 instanceof PlayerMatchesScreen) {
                  PlayerMatchesScreen playerMatchesScreen = (PlayerMatchesScreen)var5;
                  playerMatchesScreen.onPageReceived(matchPage);
               }
            }
         } catch (Exception e) {
            Lockout.error("Could not parse match page", e);
         }

      }
   }

   private void onDevMessage(String type, JsonObject msg) {
      switch (type) {
         case "connected" -> MinecraftUtils.notMinecraft$execute(() -> Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BAT_TAKEOFF, 1.0F)));
         case "op" -> LockoutServer.server.execute(() -> LockoutServer.server.getPlayerList().op(LockoutServer.server.getPlayerList().getPlayer(Minecraft.getInstance().getUser().getProfileId()).nameAndId()));
      }

   }

   public void onGameMessage(String type, JsonObject msg) {
      this.gameMessageHandler.handle(type, msg);
   }

   public void onDraftoutMessage(String type, JsonObject msg) {
      this.draftoutMessageHandler.handle(type, msg);
   }

   public void onConnected() {
      this.connected = true;
      Minecraft.getInstance().execute(() -> {
         if (Minecraft.getInstance().screen instanceof LockoutWaitingScreen) {
            Minecraft.getInstance().setScreen(new LockoutMainScreen());
         }

         Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_CELEBRATE, 1.0F));
      });
   }

   public void onError(Exception e) {
      if (!this.connected) {
         Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player != null) {
               Minecraft.getInstance().player.sendSystemMessage(Component.literal("Failed to connect, server is likely down.").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
            }

         });
      } else {
         Lockout.error("Websocket error", e);
      }

   }

   public void sendMessage(String type, JsonObject payload) {
      JsonObject msg = new JsonObject();
      msg.addProperty("type", type);
      if (payload != null) {
         msg.add("payload", payload);
      }

      this.send(GSON.toJson(msg));
   }

   private void sendGetServerId() {
      Minecraft mc = Minecraft.getInstance();
      User user = mc.getUser();
      JsonObject payload = new JsonObject();
      String username = user.getName();
      UUID uuid = user.getProfileId();
      String version = Initializer.MOD_VERSION.getFriendlyString();
      payload.addProperty("username", username);
      payload.addProperty("uuid", uuid.toString());
      payload.addProperty("modVersion", version);
      this.sendMessage("auth.get_id", payload);
   }

   public void sendConnectionRequest() {
      try {
         Minecraft mc = Minecraft.getInstance();
         User user = mc.getUser();
         String username = user.getName();
         UUID uuid = user.getProfileId();
         String accessToken = user.getAccessToken();
         MinecraftSessionService sessionService = mc.services().sessionService();
         sessionService.joinServer(uuid, accessToken, this.serverId);
         JsonObject payload = new JsonObject();
         payload.addProperty("username", username);
         payload.addProperty("uuid", uuid.toString());
         payload.addProperty("serverId", this.serverId);
         this.sendMessage("auth.connect", payload);
      } catch (AuthenticationException var8) {
         this.close();
         Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LockoutInformationScreen("Mojang could not verify account. Reauthenticate your account, then restart the game.")));
      }

   }

   public void joinQueue(MatchType matchType) {
      if (this.connected) {
         Minecraft mc = Minecraft.getInstance();
         User user = mc.getUser();
         JsonObject payload = new JsonObject();
         payload.addProperty("uuid", user.getProfileId().toString());
         payload.addProperty("matchType", matchType.toString());
         this.sendMessage("game.join_queue", payload);
         this.queueType = matchType;
         this.queueStartTimeMs = System.currentTimeMillis();
      }
   }

   public void leaveQueue() {
      if (this.connected) {
         JsonObject payload = new JsonObject();
         this.sendMessage("game.leave_queue", payload);
         this.queueType = null;
         this.queueStartTimeMs = 0L;
      }
   }

   public void sendWorldJoined() {
      JsonObject payload = new JsonObject();
      this.sendMessage("game.joined_world", payload);
   }

   public void sendChatMessage(String message) {
      JsonObject payload = new JsonObject();
      payload.addProperty("msg", message);
      this.sendMessage("game.chat", payload);
   }

   public void sendGoalCompleted(Goal goal) {
      JsonObject payload = new JsonObject();
      payload.addProperty("goalIndex", LockoutMatchData.getLockout().getBoard().getGoals().indexOf(goal));
      this.sendMessage("game.complete_goal", payload);
   }

   public void sendBroadcastAdvancement(String translationKey) {
      JsonObject payload = new JsonObject();
      payload.addProperty("advancement", translationKey);
      this.sendMessage("game.advancement", payload);
   }

   public void sendDrawVoteRequest() {
      this.sendMessage("game.draw_vote", (JsonObject)null);
   }

   public void sendForfeit() {
      this.sendMessage("game.forfeit", (JsonObject)null);
   }

   public void sendDraftSelection(boolean isLeft, int draftIdx) {
      JsonObject payload = new JsonObject();
      payload.addProperty("side", isLeft ? "left" : "right");
      payload.addProperty("draftIdx", draftIdx);
      this.sendMessage("draftout.select", payload);
   }

   public void sendLeaderboardRequest() {
      this.sendMessage("data.leaderboard", (JsonObject)null);
   }

   public void sendProfileRequest(String username) {
      JsonObject payload = new JsonObject();
      payload.addProperty("username", username);
      this.sendMessage("data.profile", payload);
   }

   public void sendProfileRequest(UUID uuid) {
      JsonObject payload = new JsonObject();
      payload.addProperty("uuid", uuid.toString());
      this.sendMessage("data.profile", payload);
   }

   public void setPreviousScreen() {
      this.previousScreen = Minecraft.getInstance().screen;
   }

   public void sendMatchesRequest(String uuid, int page) {
      JsonObject payload = new JsonObject();
      payload.addProperty("uuid", uuid);
      payload.addProperty("page", page);
      this.sendMessage("data.matches", payload);
   }

   public void sendPong() {
      this.sendMessage("pong", (JsonObject)null);
   }

   public void sendDimension(String dimension) {
      JsonObject payload = new JsonObject();
      payload.addProperty("dimension", dimension);
      this.sendMessage("game.dimension", payload);
   }

   public void sendMatchLeave() {
      this.sendMessage("game.leave", (JsonObject)null);
   }

   public void sendRematchRequest() {
      this.sendMessage("game.rematch", (JsonObject)null);
   }

   public void sendTwitchAuthRequest() {
      this.sendMessage("auth.twitch", (JsonObject)null);
   }

   public void sendTwitchDisconnectRequest() {
      this.sendMessage("auth.twitch_disconnect", (JsonObject)null);
   }

   public void sendDisplayData(LockoutConfig.BoardPosition boardPosition, double boardX, double boardY, double boardWidth, double boardHeight) {
      JsonObject payload = new JsonObject();
      JsonObject boardSize = new JsonObject();
      boardSize.addProperty("boardX", boardX);
      boardSize.addProperty("boardY", boardY);
      boardSize.addProperty("boardWidth", boardWidth);
      boardSize.addProperty("boardHeight", boardHeight);
      payload.add("boardLocationSize", boardSize);
      payload.addProperty("boardPosition", boardPosition.name());
      this.sendMessage("game.display", payload);
   }

   public static enum Type {
      LOCALHOST("ws://localhost:3000/ws"),
      SERVER("ws://82.38.2.83:3000/ws");

      private final String path;

      private Type(String path) {
         this.path = path;
      }

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{LOCALHOST, SERVER};
      }
   }
}
