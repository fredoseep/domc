package com.draftoutmc.draftout.mixin.client;

import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutDraftScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.PostGameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DebugScreenOverlay.class})
public class DebugScreenOverlayMixin {
   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void lockout$hideDebugMenu(GuiGraphicsExtractor graphics, CallbackInfo ci) {
      if (Minecraft.getInstance().screen instanceof LockoutDraftScreen || Minecraft.getInstance().screen instanceof PostGameScreen) {
         ci.cancel();
      }

   }
}
