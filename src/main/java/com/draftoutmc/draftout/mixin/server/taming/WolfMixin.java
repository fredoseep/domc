package com.draftoutmc.draftout.mixin.server.taming;

import com.draftoutmc.draftout.match.standardize.taming.TamingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Wolf.class})
public class WolfMixin {
   @Redirect(
      method = {"tryToTame"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
)
   )
   private int onTameRng(RandomSource rng, int bound) {
      Level var4 = ((Wolf)(Object)this).level();
      if (var4 instanceof ServerLevel serverLevel) {
         return TamingHelper.attempt(serverLevel, "wolf", bound);
      } else {
         return rng.nextInt(bound);
      }
   }
}
