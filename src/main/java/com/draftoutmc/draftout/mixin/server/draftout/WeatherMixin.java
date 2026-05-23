package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.standardize.weather.WeatherHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ServerLevel.class})
public class WeatherMixin {
   @Redirect(
      method = {"advanceWeatherCycle"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/valueproviders/IntProvider;sample(Lnet/minecraft/util/RandomSource;)I",
   ordinal = 0
)
   )
   private int onThunderDuration(IntProvider instance, RandomSource random) {
      return !LockoutMatchData.isInMatch() ? instance.sample(random) : WeatherHelper.thunderDuration((ServerLevel)(Object)this, instance);
   }

   @Redirect(
      method = {"advanceWeatherCycle"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/valueproviders/IntProvider;sample(Lnet/minecraft/util/RandomSource;)I",
   ordinal = 1
)
   )
   private int onThunderDelay(IntProvider instance, RandomSource random) {
      return !LockoutMatchData.isInMatch() ? instance.sample(random) : WeatherHelper.thunderDelay((ServerLevel)(Object)this, instance);
   }

   @Redirect(
      method = {"advanceWeatherCycle"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/valueproviders/IntProvider;sample(Lnet/minecraft/util/RandomSource;)I",
   ordinal = 2
)
   )
   private int onRainDuration(IntProvider instance, RandomSource random) {
      return !LockoutMatchData.isInMatch() ? instance.sample(random) : WeatherHelper.rainDuration((ServerLevel)(Object)this, instance);
   }

   @Redirect(
      method = {"advanceWeatherCycle"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/valueproviders/IntProvider;sample(Lnet/minecraft/util/RandomSource;)I",
   ordinal = 3
)
   )
   private int onRainDelay(IntProvider instance, RandomSource random) {
      return !LockoutMatchData.isInMatch() ? instance.sample(random) : WeatherHelper.rainDelay((ServerLevel)(Object)this, instance);
   }
}
