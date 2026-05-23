package com.draftoutmc.draftout.mixin.client.export;

import com.draftoutmc.draftout.client.export.GoalIconExportEnvironment;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TextureAtlas.class})
public abstract class TextureAtlasExportMixin {
   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void draftout$freezeAutomaticAtlasAnimationDuringGoalIconExport(CallbackInfo ci) {
      if (GoalIconExportEnvironment.isExportMode()) {
         ci.cancel();
      }

   }
}
