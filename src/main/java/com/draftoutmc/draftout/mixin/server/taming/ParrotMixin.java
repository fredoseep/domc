package com.draftoutmc.draftout.mixin.server.taming;

import com.draftoutmc.draftout.match.standardize.taming.TamingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Parrot.class})
public class ParrotMixin {
   @Redirect(
      method = {"mobInteract"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
)
   )
   private int onTameRng(RandomSource rng, int bound) {
      Level var4 = ((Parrot)(Object)this).level();
      if (var4 instanceof ServerLevel serverLevel) {
         return TamingHelper.attempt(serverLevel, "parrot", bound);
      } else {
         return rng.nextInt(bound);
      }
   }
}
