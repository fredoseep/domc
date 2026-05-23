package com.draftoutmc.draftout.lockout.goals.mine;

import com.draftoutmc.draftout.lockout.interfaces.MineBlockGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class MineMobSpawnerGoal extends MineBlockGoal {
   private static final List<Item> ITEMS;
   private static final Item ITEM;

   public MineMobSpawnerGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Mine Mob Spawner";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   static {
      ITEMS = List.of(Items.SPAWNER);
      ITEM = Items.SPAWNER;
   }
}
