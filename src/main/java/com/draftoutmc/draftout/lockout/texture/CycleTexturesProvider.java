package com.draftoutmc.draftout.lockout.texture;

import com.draftoutmc.draftout.LockoutConfig;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public interface CycleTexturesProvider extends CustomTextureRenderer {
   List<Identifier> getTexturesToDisplay();

   default boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      int cycleTicks = LockoutConfig.getInstance().cycleSpeed / 50;
      int mod = tick % (cycleTicks * this.getTexturesToDisplay().size());
      context.blit(RenderPipelines.GUI_TEXTURED, (Identifier)this.getTexturesToDisplay().get(mod / cycleTicks), x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      return true;
   }
}
