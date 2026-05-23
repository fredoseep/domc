package com.draftoutmc.draftout.match;

import com.draftoutmc.draftout.Lockout;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

public class WorldCreator {
   public static void createWorld(String worldName, long seed) {
      Minecraft mc = Minecraft.getInstance();
      mc.execute(() -> {
         try {
            PackRepository repo = mc.getResourcePackRepository();
            List<String> withoutUserPacks = repo.getSelectedPacks().stream().filter((pack) -> pack.isRequired() || pack.getPackSource() != PackSource.DEFAULT).map(Pack::getId).toList();
            boolean hasUserPacks = withoutUserPacks.size() != repo.getSelectedPacks().size();
            if (hasUserPacks) {
               repo.setSelected(withoutUserPacks);
               mc.reloadResourcePacks();
            }

            WorldOpenFlows flows = mc.createWorldOpenFlows();
            LevelSettings levelSettings = new LevelSettings(worldName, GameType.SURVIVAL, new LevelSettings.DifficultySettings(Difficulty.EASY, false, false), false, WorldDataConfiguration.DEFAULT);
            WorldOptions options = new WorldOptions(seed, true, false);
            Function<HolderLookup.Provider, WorldDimensions> dimensionsProvider = WorldPresets::createNormalWorldDimensions;
            flows.createFreshLevel(worldName, levelSettings, options, dimensionsProvider, new TitleScreen(false));
         } catch (Exception e) {
            Lockout.error("Could not create world", e);
         }

      });
   }

   public static String makeUniqueWorldName(String base) {
      Minecraft mc = Minecraft.getInstance();
      File gameDir = mc.gameDirectory;
      File savesDir = new File(gameDir, "saves");
      Supplier<String> name = () -> base + "-" + UUID.randomUUID().toString().substring(0, 8);
      String candidate = (String)name.get();

      for(int i = 0; i < 32; ++i) {
         File f = new File(savesDir, candidate);
         if (!savesDir.exists() || !f.exists()) {
            return candidate;
         }

         candidate = (String)name.get();
      }

      return (String)name.get();
   }
}
