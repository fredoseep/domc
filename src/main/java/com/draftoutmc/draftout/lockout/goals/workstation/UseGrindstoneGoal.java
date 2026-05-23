package com.draftoutmc.draftout.lockout.goals.workstation;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseGrindstoneGoal extends Goal {
   private final ItemStack ITEM_STACK;

   public UseGrindstoneGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.GRINDSTONE.getDefaultInstance();
   }

   public String getGoalName() {
      return "Use Grindstone";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }
}
