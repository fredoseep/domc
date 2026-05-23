package com.draftoutmc.draftout.lockout.goals.status_effect.unique;

import com.draftoutmc.draftout.lockout.goals.status_effect.GetXStatusEffectsGoal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class Get6StatusEffectsGoal extends GetXStatusEffectsGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/status_effect/6_status_effects.png");

   public Get6StatusEffectsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Get 6 Status Effects at once";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public int getAmount() {
      return 6;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
