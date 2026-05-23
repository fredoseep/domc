package com.draftoutmc.draftout.server.handlers;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;

public class AfterRespawnEventHandler implements ServerPlayerEvents.AfterRespawn {
   public void afterRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
   }
}
