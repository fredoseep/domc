package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain6UniqueFlowersGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public Obtain6UniqueFlowersGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 6;
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 6 Unique Flowers";
   }

   static {
      ITEMS = List.of(Items.DANDELION, Items.POPPY, Items.BLUE_ORCHID, Items.ALLIUM, Items.AZURE_BLUET, Items.RED_TULIP, Items.ORANGE_TULIP, Items.WHITE_TULIP, Items.PINK_TULIP, Items.OXEYE_DAISY, Items.CORNFLOWER, Items.LILY_OF_THE_VALLEY, Items.TORCHFLOWER, Items.WITHER_ROSE, Items.SUNFLOWER, Items.LILAC, Items.ROSE_BUSH, Items.PEONY, Items.CLOSED_EYEBLOSSOM, Items.OPEN_EYEBLOSSOM, Items.CACTUS_FLOWER, Items.CHORUS_FLOWER, Items.GOLDEN_DANDELION, Items.PITCHER_PLANT, Items.CHERRY_LEAVES, Items.FLOWERING_AZALEA, Items.FLOWERING_AZALEA_LEAVES, Items.MANGROVE_PROPAGULE, Items.PINK_PETALS, Items.SPORE_BLOSSOM, Items.WILDFLOWERS, Items.CACTUS_FLOWER);
   }
}
