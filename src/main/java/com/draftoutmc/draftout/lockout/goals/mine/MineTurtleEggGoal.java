package com.draftoutmc.draftout.lockout.goals.mine;

import com.draftoutmc.draftout.lockout.interfaces.MineBlockGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class MineTurtleEggGoal extends MineBlockGoal {
   private static final List<Item> ITEMS;
   private static final Item ITEM;

   public MineTurtleEggGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Mine Turtle Egg";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   static {
      ITEMS = List.of(Items.TURTLE_EGG);
      ITEM = Items.TURTLE_EGG;
   }
}
