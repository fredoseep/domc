package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainGildedBlackstone extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainGildedBlackstone(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain Gilded Blackstone";
   }

   static {
      ITEMS = List.of(Items.GILDED_BLACKSTONE);
   }
}
