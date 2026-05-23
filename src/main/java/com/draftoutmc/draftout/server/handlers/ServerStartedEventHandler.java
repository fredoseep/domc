package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.standardize.SurfacePortalForcing;
import com.draftoutmc.draftout.match.standardize.barter.BarterTracker;
import com.draftoutmc.draftout.server.LockoutServer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartedEventHandler implements ServerLifecycleEvents.ServerStarted {
   public void onServerStarted(MinecraftServer server) {
      long seed = server.overworld().getSeed();
      BarterTracker.initialize(seed);
      SurfacePortalForcing.reset();
      server.execute(() -> {
         LockoutServer.server = server;
         if (LockoutMatchData.isInMatch()) {
            server.tickRateManager().setFrozen(true);
         }

      });
   }
}
