package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class PutWolfArmorOnWolfGoal extends Goal {
   private final ItemStack ITEM;

   public PutWolfArmorOnWolfGoal(String id, String data) {
      super(id, data);
      this.ITEM = Items.WOLF_ARMOR.getDefaultInstance();
   }

   public String getGoalName() {
      return "Put Wolf Armor on Wolf";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM;
   }
}
