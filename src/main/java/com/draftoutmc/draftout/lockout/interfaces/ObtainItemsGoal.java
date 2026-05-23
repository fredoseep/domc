package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CycleItemTexturesProvider;
import java.util.List;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class ObtainItemsGoal extends Goal implements CycleItemTexturesProvider {
   public ObtainItemsGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Item> getItems();

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public List<Item> getItemsToDisplay() {
      return this.getItems();
   }

   public abstract boolean satisfiedBy(Inventory var1);
}
