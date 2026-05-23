package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class FillArmorStandGoal extends Goal {
   private static final Item ITEM;

   public FillArmorStandGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Fill an Armor Stand";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   static {
      ITEM = Items.ARMOR_STAND;
   }
}
