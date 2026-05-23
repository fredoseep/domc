package com.draftoutmc.draftout.lockout.goals.status_effect.unique;

import com.draftoutmc.draftout.lockout.goals.status_effect.GetXStatusEffectsGoal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class Get4StatusEffectsGoal extends GetXStatusEffectsGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/status_effect/4_status_effects.png");

   public Get4StatusEffectsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Get 4 Status Effects at once";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public int getAmount() {
      return 4;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
