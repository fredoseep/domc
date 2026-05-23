package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class ReachXPLevelGoal extends Goal implements TextureProvider, RequiresAmount {
   public ReachXPLevelGoal(String id, String data) {
      super(id, data);
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }
}
