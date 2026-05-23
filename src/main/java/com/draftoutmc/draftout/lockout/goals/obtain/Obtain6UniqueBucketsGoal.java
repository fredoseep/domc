package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain6UniqueBucketsGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public Obtain6UniqueBucketsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 6;
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 6 Unique Buckets";
   }

   static {
      ITEMS = List.of(Items.BUCKET, Items.WATER_BUCKET, Items.COD_BUCKET, Items.SALMON_BUCKET, Items.LAVA_BUCKET, Items.MILK_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.AXOLOTL_BUCKET, Items.POWDER_SNOW_BUCKET, Items.TADPOLE_BUCKET);
   }
}
