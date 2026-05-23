package com.draftoutmc.draftout.match.standardize.composter;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;

public class ComposterHelper {
   public static double attempt(ServerLevel level) {
      int attemptIndex = ComposterSavedData.get(level).getAndIncrement();
      long seed = level.getSeed() ^ -4259325567209780784L ^ (long)attemptIndex * -7046029254386353131L;
      return (new Random(seed)).nextDouble();
   }
}
