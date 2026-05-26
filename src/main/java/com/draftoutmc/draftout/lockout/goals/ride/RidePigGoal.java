package com.draftoutmc.draftout.lockout.goals.ride;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class RidePigGoal extends Goal {
   private final ItemStack ITEM_STACK;

   public RidePigGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.CARROT_ON_A_STICK.getDefaultInstance();
   }

   public String getGoalName() {
      return "Ride Pig with Carrot on a Stick";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }
}
