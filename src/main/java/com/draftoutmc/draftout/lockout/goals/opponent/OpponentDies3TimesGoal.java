package com.draftoutmc.draftout.lockout.goals.opponent;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class OpponentDies3TimesGoal extends Goal implements CustomTextureRenderer {
   private final ItemStack DISPLAY_ITEM_STACK;
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/opponent/no_death.png");

   public OpponentDies3TimesGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.PLAYER_HEAD.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.setCount(3);
   }

   public String getGoalName() {
      return "Opponent dies 3 times";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      context.itemDecorations(Minecraft.getInstance().font, this.DISPLAY_ITEM_STACK, x, y);
      return true;
   }
}
