package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain4UniqueSaplingsGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public Obtain4UniqueSaplingsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 4;
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 4 Unique Saplings";
   }

   static {
      ITEMS = List.of(Items.OAK_SAPLING, Items.ACACIA_SAPLING, Items.BIRCH_SAPLING, Items.CHERRY_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING, Items.SPRUCE_SAPLING, Items.MANGROVE_PROPAGULE, Items.PALE_OAK_SAPLING, Items.AZALEA, Items.FLOWERING_AZALEA);
   }
}
