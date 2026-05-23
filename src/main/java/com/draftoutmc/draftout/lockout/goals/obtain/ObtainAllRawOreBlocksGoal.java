package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainAllRawOreBlocksGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainAllRawOreBlocksGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain every type of Raw Ore Block";
   }

   static {
      ITEMS = List.of(Items.RAW_COPPER_BLOCK, Items.RAW_GOLD_BLOCK, Items.RAW_IRON_BLOCK);
   }
}
