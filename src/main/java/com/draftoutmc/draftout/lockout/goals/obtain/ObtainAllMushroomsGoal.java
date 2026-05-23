package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainAllMushroomsGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainAllMushroomsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain every type of Mushroom";
   }

   static {
      ITEMS = List.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM, Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS);
   }
}
