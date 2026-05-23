package com.draftoutmc.draftout.lockout.goals.lead;

import com.draftoutmc.draftout.lockout.interfaces.LeashMobGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class LeashIronGolem extends LeashMobGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/leash_iron_golem.png");

   public LeashIronGolem(String id, String data) {
      super(id, data);
   }

   public EntityType<?> getMob() {
      return EntityType.IRON_GOLEM;
   }

   public String getGoalName() {
      return "Leash an Iron Golem";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
