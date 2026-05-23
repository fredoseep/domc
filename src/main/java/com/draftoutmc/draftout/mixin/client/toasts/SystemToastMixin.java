package com.draftoutmc.draftout.mixin.client.toasts;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.SystemToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SystemToast.class})
public class SystemToastMixin {
   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onDraw(GuiGraphicsExtractor context, Font textRenderer, long startTime, CallbackInfo ci) {
      if (Lockout.exists(LockoutMatchData.getLockout())) {
         ci.cancel();
      }

   }
}
