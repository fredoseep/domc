package com.draftoutmc.draftout.lockout.goals.workstation;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseSmithingTableGoal extends Goal {
   private final ItemStack ITEM_STACK;

   public UseSmithingTableGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.SMITHING_TABLE.getDefaultInstance();
   }

   public String getGoalName() {
      return "Use Smithing Table";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }
}
