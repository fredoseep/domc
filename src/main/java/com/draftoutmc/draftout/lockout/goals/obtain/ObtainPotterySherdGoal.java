package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ObtainPotterySherdGoal extends ObtainSomeOfTheItemsGoal {
   private final List<Item> ITEMS;

   public ObtainPotterySherdGoal(String id, String data) {
      super(id, data);
      this.ITEMS = BuiltInRegistries.ITEM.stream().filter((item) -> item.getDefaultInstance().is(ItemTags.DECORATED_POT_SHERDS)).toList();
   }

   public List<Item> getItems() {
      return this.ITEMS;
   }

   public String getGoalName() {
      return "Obtain Pottery Sherd";
   }

   public int getAmount() {
      return 1;
   }
}
