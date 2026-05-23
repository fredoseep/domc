package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class SpyOnMobGoal extends Goal implements TextureProvider {
   private final ItemStack DISPLAY_ITEM_STACK;

   public SpyOnMobGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.SPYGLASS.getDefaultInstance();
   }

   public abstract EntityType<?> getMob();

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }
}
