package com.draftoutmc.draftout.lockout.goals;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class EmptyGoal extends Goal {
   private final ItemStack display;

   public EmptyGoal(String id, String data) {
      super(id, data);
      this.display = Items.AIR.getDefaultInstance();
   }

   public String getGoalName() {
      String var10000 = this.getId();
      return var10000 + (this.getData() != null && !this.getData().equals("null") ? "(" + this.getData() + ")" : "");
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.display;
   }
}
