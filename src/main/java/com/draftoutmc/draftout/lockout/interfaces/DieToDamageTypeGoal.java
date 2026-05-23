package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class DieToDamageTypeGoal extends Goal implements TextureProvider {
   public DieToDamageTypeGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<ResourceKey<DamageType>> getDamageRegistryKeys();

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }
}
