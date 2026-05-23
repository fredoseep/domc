package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jspecify.annotations.Nullable;

public abstract class DrinkPotionGoal extends Goal {
   private final ItemStack displayItem;

   public DrinkPotionGoal(String id, String data) {
      super(id, data);
      this.displayItem = PotionContents.createItemStack(Items.POTION, this.getPotion());
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.displayItem;
   }

   public abstract Holder<Potion> getPotion();
}
