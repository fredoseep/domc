package com.draftoutmc.draftout.mixin.server.taming;

import com.draftoutmc.draftout.match.standardize.taming.TamingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({RunAroundLikeCrazyGoal.class})
public class RunAroundLikeCrazyGoalMixin {
   @Final
   @Shadow
   private AbstractHorse horse;

   @Redirect(
      method = {"tick"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/RandomSource;nextInt(I)I",
   ordinal = 0
)
   )
   private int onThrottleRng(RandomSource rng, int bound) {
      AbstractHorse var5 = this.horse;
      if (var5 instanceof Horse horse) {
         Level var6 = horse.level();
         if (var6 instanceof ServerLevel serverLevel) {
            return TamingHelper.attempt(serverLevel, "horse_throttle", bound);
         }
      }

      return rng.nextInt(bound);
   }

   @Redirect(
      method = {"tick"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/RandomSource;nextInt(I)I",
   ordinal = 1
)
   )
   private int onTameRng(RandomSource rng, int bound) {
      AbstractHorse var5 = this.horse;
      if (var5 instanceof Horse horse) {
         Level var6 = horse.level();
         if (var6 instanceof ServerLevel serverLevel) {
            return TamingHelper.attempt(serverLevel, "horse", bound);
         }
      }

      return rng.nextInt(bound);
   }
}
