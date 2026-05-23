package com.draftoutmc.draftout.lockout.goals.mine;

import com.draftoutmc.draftout.lockout.interfaces.MineBlockGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class MineCrafterGoal extends MineBlockGoal {
   private static final List<Item> ITEMS;
   private static final Item ITEM;

   public MineCrafterGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Mine Crafter";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   static {
      ITEMS = List.of(Items.CRAFTER);
      ITEM = Items.CRAFTER;
   }
}
