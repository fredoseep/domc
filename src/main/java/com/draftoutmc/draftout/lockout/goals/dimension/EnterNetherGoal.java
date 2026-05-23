package com.draftoutmc.draftout.lockout.goals.dimension;

import com.draftoutmc.draftout.lockout.interfaces.EnterDimensionGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class EnterNetherGoal extends EnterDimensionGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/nether_portal.png");

   public EnterNetherGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Enter Nether";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public ResourceKey<Level> getWorldRegistryKey() {
      return ServerLevel.NETHER;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
