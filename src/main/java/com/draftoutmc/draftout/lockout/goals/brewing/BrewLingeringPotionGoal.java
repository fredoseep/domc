package com.draftoutmc.draftout.lockout.goals.brewing;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class BrewLingeringPotionGoal extends Goal {
   private final ItemStack ITEM;

   public BrewLingeringPotionGoal(String id, String data) {
      super(id, data);
      this.ITEM = Items.LINGERING_POTION.getDefaultInstance();
   }

   public String getGoalName() {
      return "Brew a Lingering Potion";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM;
   }
}
