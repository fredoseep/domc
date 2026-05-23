package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class BreedAnimalGoal extends Goal {
   public BreedAnimalGoal(String id, String data) {
      super(id, data);
   }

   public abstract EntityType<?> getAnimal();

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }
}
