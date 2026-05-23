package com.draftoutmc.draftout.mixin.client.gui;

import com.draftoutmc.draftout.client.gui.ranked.elements.QueueInfoRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
public class ScreenMixin {
   @Inject(
      method = {"extractRenderStateWithTooltipAndSubtitles"},
      at = {@At("TAIL")}
   )
   public void renderQueueInfo(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
      QueueInfoRenderer.render(graphics);
   }
}
