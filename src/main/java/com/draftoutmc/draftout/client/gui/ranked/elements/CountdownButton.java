package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.draftoutmc.draftout.Utility;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CountdownButton extends Button.Plain {
   private final long enabledTimeMs;

   protected CountdownButton(long countdownMs, int x, int y, int width, int height, Component message, Button.OnPress onPress, Button.CreateNarration createNarration) {
      super(x, y, width, height, message, onPress, createNarration);
      this.enabledTimeMs = System.currentTimeMillis() + countdownMs;
   }

   public static CountdownButton fromPlain(Button.Plain button, long countdownMs) {
      return new CountdownButton(countdownMs, button.getX(), button.getY(), button.getWidth(), button.getHeight(), button.getMessage(), button.onPress, button.createNarration);
   }

   protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      this.active = System.currentTimeMillis() >= this.enabledTimeMs;
      super.extractContents(graphics, mouseX, mouseY, a);
      if (!this.active) {
         String timer = Utility.msToSeconds(this.enabledTimeMs - System.currentTimeMillis(), 1);
         Font font = Minecraft.getInstance().font;
         graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 2130706432);
         MutableComponent var10002 = Component.literal(timer + "s");
         int var10003 = this.getX() + this.getWidth() / 2;
         int var10004 = this.getY() + this.getHeight() / 2;
         Objects.requireNonNull(font);
         graphics.centeredText(font, var10002, var10003, var10004 - 9 / 2, -1);
      }

   }
}
