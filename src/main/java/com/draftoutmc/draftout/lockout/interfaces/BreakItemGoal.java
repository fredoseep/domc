package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class BreakItemGoal extends Goal implements TextureProvider {
   public BreakItemGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Item> getItems();

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }
}
