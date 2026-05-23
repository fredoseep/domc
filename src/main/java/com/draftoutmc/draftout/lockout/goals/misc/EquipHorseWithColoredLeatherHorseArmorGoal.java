package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import org.jspecify.annotations.Nullable;

public class EquipHorseWithColoredLeatherHorseArmorGoal extends Goal {
   private final Item ITEM;
   private final ItemStack DISPLAY_ITEM_STACK;
   private final int COLOR;
   private final String GOAL_NAME;

   public EquipHorseWithColoredLeatherHorseArmorGoal(String id, String data) {
      super(id, data);
      this.ITEM = Items.LEATHER_HORSE_ARMOR;
      DyeColor DYE_COLOR = GoalDataConstants.getDyeColor(data);
      this.COLOR = GoalDataConstants.getDyeColorValue(DYE_COLOR);
      this.DISPLAY_ITEM_STACK = this.ITEM.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.set(DataComponents.DYED_COLOR, new DyedItemColor(this.COLOR));
      this.GOAL_NAME = "Equip Horse with " + GoalDataConstants.getDyeColorFormatted(DYE_COLOR) + " Leather Horse Armor";
   }

   public String getGoalName() {
      return this.GOAL_NAME;
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }
}
