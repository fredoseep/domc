package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EatAllSoupsGoal extends ConsumeItemsGoal {
   private static final List<Item> ITEMS;

   public EatAllSoupsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Eat every type of Soup";
   }

   static {
      ITEMS = List.of(Items.SUSPICIOUS_STEW, Items.MUSHROOM_STEW, Items.RABBIT_STEW, Items.BEETROOT_SOUP);
   }
}
