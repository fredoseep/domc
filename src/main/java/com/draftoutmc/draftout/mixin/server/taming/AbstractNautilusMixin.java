package com.draftoutmc.draftout.mixin.server.taming;

import com.draftoutmc.draftout.match.standardize.taming.TamingHelper;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({AbstractNautilus.class})
public class AbstractNautilusMixin {
   @Redirect(
      method = {"tryToTame"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
)
   )
   private int onTameRng(RandomSource rng, int bound) {
      AbstractNautilus abstractNautilus = (AbstractNautilus)(Object)this;
      if (!(abstractNautilus instanceof Nautilus)) {
         return rng.nextInt(bound);
      } else {
         Level var4 = ((AbstractNautilus)(Object)this).level();
         if (var4 instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)var4;
            return TamingHelper.attempt(serverLevel, "nautilus", bound);
         } else {
            return rng.nextInt(bound);
         }
      }
   }
}
