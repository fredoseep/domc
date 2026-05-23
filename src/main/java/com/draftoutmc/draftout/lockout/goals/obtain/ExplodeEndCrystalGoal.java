package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ExplodeEndCrystalGoal extends Goal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/end_crystal.png");

   public ExplodeEndCrystalGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Explode End Crystal";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      return TextureProvider.super.renderTexture(context, x, y, tick);
   }
}
