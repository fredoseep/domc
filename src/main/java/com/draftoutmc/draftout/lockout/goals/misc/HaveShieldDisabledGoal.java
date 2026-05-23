package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class HaveShieldDisabledGoal extends Goal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/disable_shield.png");

   public HaveShieldDisabledGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Have your Shield disabled";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
