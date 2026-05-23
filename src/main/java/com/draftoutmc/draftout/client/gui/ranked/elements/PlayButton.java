package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.draftoutmc.draftout.Constants;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.match.data.RankedRank;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public class PlayButton extends Button {
   private String text;
   private Identifier sprite;

   public PlayButton(int x, int y, int width, int height, String message, Button.OnPress onPress, Identifier sprite) {
      super(x, y, width, height, Component.literal(message), onPress, DEFAULT_NARRATION);
      this.text = message;
      this.sprite = sprite;
   }

   protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      Font font = Minecraft.getInstance().font;
      int tint = this.isHoveredOrFocused() ? Utility.brightness(-1, 0.6F) : -1;
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Constants.BUTTON_IDENTIFIER, this.getX(), this.getY(), this.width, this.height, tint);
      int edgeGap = 8;
      int size = Math.min(this.width, this.height) - edgeGap * 2;
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX() + edgeGap, this.getY() + edgeGap, size, size, tint);
      int textColor = RankedRank.Colors.DIAMOND_COLOR.getRGB();
      int color = this.isHoveredOrFocused() ? Utility.brightness(textColor, 0.6F) : textColor;
      MutableComponent var10002 = Component.literal(this.text).withColor(color);
      int var10003 = this.getX() + edgeGap * 2 + size;
      int var10004 = this.getY() + this.height / 2;
      Objects.requireNonNull(font);
      graphics.text(font, var10002, var10003, var10004 - 9 / 2, -1);
   }

   @Generated
   public void setText(String text) {
      this.text = text;
   }
}
