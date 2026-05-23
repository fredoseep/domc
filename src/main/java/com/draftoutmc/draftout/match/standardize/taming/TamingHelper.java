package com.draftoutmc.draftout.match.standardize.taming;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;

public class TamingHelper {
   public static int attempt(ServerLevel level, String mobType, int bound) {
      int attemptIndex = TamingSavedData.get(level).getAndIncrement(mobType);
      long seed = level.getSeed() ^ (long)mobType.hashCode() * -2977037099943890807L ^ (long)attemptIndex;
      return (new Random(seed)).nextInt(bound);
   }
}
