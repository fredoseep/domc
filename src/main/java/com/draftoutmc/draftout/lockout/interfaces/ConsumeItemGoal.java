package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class ConsumeItemGoal extends Goal {
   public ConsumeItemGoal(String id, String data) {
      super(id, data);
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.getItem().getDefaultInstance();
   }

   public abstract Item getItem();
}
