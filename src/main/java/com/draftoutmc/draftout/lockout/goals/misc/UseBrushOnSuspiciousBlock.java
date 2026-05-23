package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrameProvider;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseBrushOnSuspiciousBlock extends Goal implements CustomTextureRenderer, GoalIconFrameProvider {
   private final List<ItemStack> SUSPICIOUS_BLOCKS;
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/brush_overlay.png");

   public UseBrushOnSuspiciousBlock(String id, String data) {
      super(id, data);
      this.SUSPICIOUS_BLOCKS = List.of(Items.SUSPICIOUS_GRAVEL.getDefaultInstance(), Items.SUSPICIOUS_SAND.getDefaultInstance());
   }

   public String getGoalName() {
      return "Use Brush on Suspicious Sand/Gravel";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      int cycleTicks = LockoutConfig.getInstance().cycleSpeed / 50;
      int mod = tick % (cycleTicks * this.SUSPICIOUS_BLOCKS.size());
      context.item((ItemStack)this.SUSPICIOUS_BLOCKS.get(mod / cycleTicks), x, y);
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      return true;
   }

   public List<GoalIconFrame> getGoalIconFrames() {
      return List.of(GoalIconFrame.renderGoalAtTick(this, 0), GoalIconFrame.renderGoalAtTick(this, 60));
   }
}
