package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainBucketOfTropicalFishGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainBucketOfTropicalFishGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain Bucket of Tropical Fish";
   }

   static {
      ITEMS = List.of(Items.TROPICAL_FISH_BUCKET);
   }
}
