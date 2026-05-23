package com.draftoutmc.draftout.lockout.goals.workstation;

import com.draftoutmc.draftout.lockout.interfaces.IncrementStatGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseEnchantingTableGoal extends IncrementStatGoal {
   private static final List<Identifier> STATS;
   private final ItemStack ITEM_STACK;

   public UseEnchantingTableGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.ENCHANTING_TABLE.getDefaultInstance();
   }

   public List<Identifier> getStats() {
      return STATS;
   }

   public String getGoalName() {
      return "Use Enchanting Table";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   static {
      STATS = List.of(Stats.ENCHANT_ITEM);
   }
}
