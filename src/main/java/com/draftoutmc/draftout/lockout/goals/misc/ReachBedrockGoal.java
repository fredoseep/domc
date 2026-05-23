package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class ReachBedrockGoal extends Goal implements CustomTextureRenderer {
   private final ItemStack ITEM_STACK;
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/down_arrow.png");

   public ReachBedrockGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.BEDROCK.getDefaultInstance();
   }

   public String getGoalName() {
      return "Reach Bedrock";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.item(this.ITEM_STACK, x, y);
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      return true;
   }
}
