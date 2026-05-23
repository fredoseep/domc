package com.draftoutmc.draftout.lockout.texture;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public interface TextureProvider extends CustomTextureRenderer {
   Identifier getTextureIdentifier();

   default boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, this.getTextureIdentifier(), x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      return true;
   }
}
