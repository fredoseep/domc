package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class FillDecoratedPotGoal extends Goal {
   private final ItemStack ITEM;

   public FillDecoratedPotGoal(String id, String data) {
      super(id, data);
      this.ITEM = Items.DECORATED_POT.getDefaultInstance();
   }

   public String getGoalName() {
      return "Fill a Decorated Pot";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM;
   }
}
