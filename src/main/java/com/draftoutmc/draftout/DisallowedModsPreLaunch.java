package com.draftoutmc.draftout;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisallowedModsPreLaunch implements PreLaunchEntrypoint {
   private static final Logger logger = LoggerFactory.getLogger(DisallowedModsPreLaunch.class);
   private static final Set<String> ALLOWED_MODS = Set.of("draftout", "sodium", "lithium", "planifolia", "speedrunigt", "forceport", "hermes-core", "hermes", "boundlesswindow", "standardsettings");

   public void onPreLaunch() {
      List<String> disallowed = scanDisallowedModIds();
      if (!disallowed.isEmpty()) {
         String message = buildCrashMessage(disallowed);
         logger.error(message);
         throw new IllegalStateException(message);
      }
   }

   public static List<String> scanDisallowedModIds() {
      Path modsFolder = FabricLoader.getInstance().getGameDir().resolve("mods").toAbsolutePath().normalize();
      return FabricLoader.getInstance().getAllMods().stream().filter((mod) -> isModInModsFolder(mod, modsFolder)).filter((mod) -> !ALLOWED_MODS.contains(mod.getMetadata().getId())).map((mod) -> mod.getMetadata().getId()).distinct().sorted().toList();
   }

   public static String buildCrashMessage(List<String> disallowedIds) {
      int var10000 = disallowedIds.size();
      return "Draftout blocked launch because disallowed mods were found in your mods folder.\nDisallowed mods (" + var10000 + "): " + String.join(", ", disallowedIds) + "\nRemove the disallowed mods and restart Minecraft.";
   }

   static boolean isModInModsFolder(ModContainer mod, Path modsFolder) {
      try {
         Path origin = ((Path)mod.getOrigin().getPaths().getFirst()).toAbsolutePath().normalize();
         return origin.startsWith(modsFolder);
      } catch (Exception var3) {
         return false;
      }
   }
}
