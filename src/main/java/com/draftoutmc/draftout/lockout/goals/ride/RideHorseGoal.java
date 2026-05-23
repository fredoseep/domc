package com.draftoutmc.draftout.lockout.goals.ride;

import com.draftoutmc.draftout.lockout.interfaces.RideEntityGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class RideHorseGoal extends RideEntityGoal {
   private final ItemStack ITEM_STACK;

   public RideHorseGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.SADDLE.getDefaultInstance();
   }

   public EntityType<?> getEntityType() {
      return EntityType.HORSE;
   }

   public String getGoalName() {
      return "Ride a Horse";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }
}
