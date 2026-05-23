package com.draftoutmc.draftout.match.standardize.weather;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;

public class WeatherHelper {
   private static final long THUNDER_DURATION_MIX = -6795153568590067944L;
   private static final long THUNDER_DELAY_MIX = -5565370630359541719L;
   private static final long RAIN_DURATION_MIX = -4335587696407205574L;
   private static final long RAIN_DELAY_MIX = -3105805857671529909L;

   public static int thunderDuration(ServerLevel level, IntProvider provider) {
      return sample(level, WeatherSavedData.get(level).getAndIncrementThunderDuration(), -6795153568590067944L, provider);
   }

   public static int thunderDelay(ServerLevel level, IntProvider provider) {
      return sample(level, WeatherSavedData.get(level).getAndIncrementThunderDelay(), -5565370630359541719L, provider);
   }

   public static int rainDuration(ServerLevel level, IntProvider provider) {
      return sample(level, WeatherSavedData.get(level).getAndIncrementRainDuration(), -4335587696407205574L, provider);
   }

   public static int rainDelay(ServerLevel level, IntProvider provider) {
      return sample(level, WeatherSavedData.get(level).getAndIncrementRainDelay(), -3105805857671529909L, provider);
   }

   private static int sample(ServerLevel level, int attemptIndex, long mix, IntProvider provider) {
      long seed = level.getSeed() ^ mix ^ (long)attemptIndex * -7046029254386353131L;
      Random random = new Random(seed);
      int min = provider.minInclusive();
      int max = provider.maxInclusive();
      return min + random.nextInt(max - min + 1);
   }
}
