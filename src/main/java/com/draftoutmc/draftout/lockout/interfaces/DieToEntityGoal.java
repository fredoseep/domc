package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class DieToEntityGoal extends Goal {
   public DieToEntityGoal(String id, String data) {
      super(id, data);
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public abstract EntityType getEntityType();
}
