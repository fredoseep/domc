package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class LeashUniqueMobsGoal extends Goal implements RequiresAmount {
   private final ItemStack DISPLAY_ITEM_STACK;

   public LeashUniqueMobsGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.LEAD.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.setCount(this.getAmount());
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }

   public String getGoalName() {
      return String.format("Leash %d unique Mobs at once", this.getAmount());
   }
}
