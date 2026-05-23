package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain4UniqueSeedsGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public Obtain4UniqueSeedsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 4 Unique Seeds";
   }

   public int getAmount() {
      return 4;
   }

   static {
      ITEMS = List.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.TORCHFLOWER_SEEDS);
   }
}
