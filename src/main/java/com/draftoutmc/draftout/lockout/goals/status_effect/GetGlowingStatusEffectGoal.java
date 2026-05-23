package com.draftoutmc.draftout.lockout.goals.status_effect;

import com.draftoutmc.draftout.lockout.interfaces.StatusEffectGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GetGlowingStatusEffectGoal extends StatusEffectGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/status_effect/glowing.png");

   public GetGlowingStatusEffectGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Get Glowing";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public MobEffect getStatusEffect() {
      return (MobEffect)MobEffects.GLOWING.value();
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
