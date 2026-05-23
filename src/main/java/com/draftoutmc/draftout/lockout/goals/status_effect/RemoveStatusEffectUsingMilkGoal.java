package com.draftoutmc.draftout.lockout.goals.status_effect;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class RemoveStatusEffectUsingMilkGoal extends Goal {
   private final ItemStack ITEM_STACK;

   public RemoveStatusEffectUsingMilkGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.MILK_BUCKET.getDefaultInstance();
   }

   public String getGoalName() {
      return "Remove Effect using Milk Bucket";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }
}
