package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class PlayerChangeLevelEventHandler implements ServerEntityLevelChangeEvents.AfterPlayerChange {
   public void afterChangeLevel(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
      if (LockoutMatchData.isInMatch()) {
         ResourceKey<Level> dim = destination.dimension();
         String dimension;
         if (dim.equals(Level.OVERWORLD)) {
            dimension = "overworld";
         } else if (dim.equals(Level.NETHER)) {
            dimension = "nether";
         } else {
            if (!dim.equals(Level.END)) {
               return;
            }

            dimension = "end";
         }

         ServerConnection.getInstance().sendDimension(dimension);
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         matchData.players().stream().filter((lmp) -> lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst().ifPresent((lmp) -> lmp.dimension(dimension));
      }
   }
}
