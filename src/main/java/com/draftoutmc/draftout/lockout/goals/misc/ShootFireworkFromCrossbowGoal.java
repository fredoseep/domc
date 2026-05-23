package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ShootFireworkFromCrossbowGoal extends Goal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/item/crossbow_firework.png");

   public ShootFireworkFromCrossbowGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Shoot Firework from Crossbow";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
