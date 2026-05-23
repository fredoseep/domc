package com.draftoutmc.draftout;

import com.draftoutmc.draftout.lockout.DefaultGoalRegister;
import com.draftoutmc.draftout.network.Networking;
import com.draftoutmc.draftout.server.LockoutServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;

public class Initializer implements ModInitializer {
   public static Version MOD_VERSION;

   public void onInitialize() {
      MOD_VERSION = ((ModContainer)FabricLoader.getInstance().getModContainer("draftout").get()).getMetadata().getVersion();
      LockoutConfig.load();
      Networking.registerPayloads();
      DefaultGoalRegister.registerGoals();
      LockoutServer.initializeServer();
   }
}
