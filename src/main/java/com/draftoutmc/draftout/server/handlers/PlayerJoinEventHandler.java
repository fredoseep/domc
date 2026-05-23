package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.network.StartLockoutPayload;
import com.draftoutmc.draftout.network.UpdateTooltipPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;

public class PlayerJoinEventHandler implements ServerPlayConnectionEvents.Join {
   public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender packetSender, MinecraftServer minecraftServer) {
      Lockout lockout = LockoutMatchData.getLockout();
      ServerPlayer player = handler.getPlayer();
      if (Lockout.isLockoutRunning(lockout)) {
         if (lockout.isLockoutPlayer(player.getUUID())) {
            LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal instanceof HasTooltipInfo) {
                  HasTooltipInfo hasTooltipInfo = (HasTooltipInfo)goal;
                  UpdateTooltipPayload payload = new UpdateTooltipPayload(goal.getId(), String.join("\n", hasTooltipInfo.getTooltip(team, player)), hasTooltipInfo.getItems(team, player));
                  ServerPlayNetworking.send(player, payload);
               }
            }

            player.setGameMode(GameType.SURVIVAL);
         } else {
            player.setGameMode(GameType.SPECTATOR);
            player.sendSystemMessage(Component.literal("You are spectating this match.").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
         }

         ServerPlayNetworking.send(player, lockout.getTeamsGoalsPacket());
         if (lockout.hasStarted()) {
            ServerPlayNetworking.send(player, StartLockoutPayload.INSTANCE);
         }

      }
   }
}
