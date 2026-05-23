package com.draftoutmc.draftout.client.gui.ranked.elements;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class CycleSpeedSliderWidget extends AbstractSliderButton {
   private static final int MIN_MS = 1000;
   private static final int MAX_MS = 5000;
   private static final int STEP_MS = 250;
   private static final int TOTAL_STEPS = 16;
   private final Consumer<Integer> onUpdate;

   public CycleSpeedSliderWidget(int x, int y, int width, int height, int initialMs, Consumer<Integer> onUpdate) {
      super(x, y, width, height, Component.empty(), mapMsToSlider(initialMs));
      this.onUpdate = onUpdate;
      this.updateMessage();
   }

   private static double mapMsToSlider(int ms) {
      int step = (ms - 1000) / 250;
      return (double)step / (double)16.0F;
   }

   protected void updateMessage() {
      Object[] var10002 = new Object[]{(double)this.getSnappedMs() / (double)1000.0F};
      this.setMessage(Component.literal("Goal Cycle Speed: " + String.format("%.2fs", var10002)));
   }

   protected void applyValue() {
      int step = (int)Math.round(this.value * (double)16.0F);
      this.value = (double)step / (double)16.0F;
      this.updateMessage();
      this.onUpdate.accept(this.getSnappedMs());
   }

   private int getSnappedMs() {
      int step = (int)Math.round(this.value * (double)16.0F);
      return 1000 + step * 250;
   }
}
