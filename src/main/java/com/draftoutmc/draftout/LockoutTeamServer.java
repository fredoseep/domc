package com.draftoutmc.draftout;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.network.UpdateTooltipPayload;
import com.draftoutmc.draftout.server.LockoutServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

public class LockoutTeamServer extends LockoutTeam {
   private final List<UUID> players = new ArrayList();
   private final Map<UUID, String> playerNameMap = new HashMap();

   public LockoutTeamServer(Map<String, UUID> players, int color) {
      super(players.keySet().stream().toList(), color);

      for(Map.Entry<String, UUID> e : players.entrySet()) {
         this.players.add((UUID)e.getValue());
         this.playerNameMap.put((UUID)e.getValue(), (String)e.getKey());
      }

   }



   public MinecraftServer getServer() {
      return LockoutServer.server;
   }

   public String getPlayerName(UUID uuid) {
      return (String)this.playerNameMap.get(uuid);
   }

   public void sendMessage(String message) {
      this.sendMessage((Component)Component.literal(message));
   }

   public void sendMessage(Component message) {
      for(UUID uuid : this.players) {
         ServerPlayer player = this.getServer().getPlayerList().getPlayer(uuid);
         if (player != null) {
            player.sendSystemMessage(message);
         }
      }

   }

   public <T extends Goal & HasTooltipInfo> void sendTooltipUpdate(T goal) {
      for(UUID playerId : this.players) {
         ServerPlayer player = this.getServer().getPlayerList().getPlayer(playerId);
         if (player != null) {
            UpdateTooltipPayload payload = new UpdateTooltipPayload(goal.getId(), String.join("\n", (goal).getTooltip(this, player)), (goal).getItems(this, player));
            ServerPlayNetworking.send(player, payload);
         }
      }

   }

   public <T extends Goal & HasTooltipInfo> void sendTooltipUpdate(T goal, Player player) {
      if (player != null) {
         ServerPlayer serverPlayer = this.getServer().getPlayerList().getPlayer(player.getUUID());
         if (serverPlayer != null) {
            UpdateTooltipPayload payload = new UpdateTooltipPayload(goal.getId(), String.join("\n", (goal).getTooltip(this, serverPlayer)), (goal).getItems(this, player));
            ServerPlayNetworking.send(serverPlayer, payload);
         }

      }
   }

   @Generated
   public List<UUID> getPlayers() {
      return this.players;
   }
}
