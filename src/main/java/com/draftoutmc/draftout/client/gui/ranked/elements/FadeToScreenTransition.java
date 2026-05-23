package com.draftoutmc.draftout.client.gui.ranked.elements;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class FadeToScreenTransition extends Screen {
   private final Supplier<Screen> next;
   private long openedAt;
   private final long transitionTimeMs;

   public FadeToScreenTransition(Supplier<Screen> next, long transitionTimeMs) {
      super(Component.empty());
      this.next = next;
      this.transitionTimeMs = transitionTimeMs;
   }

   public void init() {
      super.init();
      this.openedAt = System.currentTimeMillis();
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return ((Screen)this.next.get()).shouldCloseOnEsc();
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
      float t = Math.min(1.0F, (float)(System.currentTimeMillis() - this.openedAt) / (float)this.transitionTimeMs);
      t = t * t * (3.0F - 2.0F * t);
      int alpha = (int)(t * 255.0F);
      graphics.fill(0, 0, this.width, this.height, ARGB.color(alpha, 0, 0, 0));
      if (alpha >= 255) {
         Minecraft.getInstance().setScreen((Screen)this.next.get());
      }

   }
}
