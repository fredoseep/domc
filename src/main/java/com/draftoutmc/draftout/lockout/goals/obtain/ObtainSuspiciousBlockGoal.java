package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainSuspiciousBlockGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainSuspiciousBlockGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain Suspicious Block";
   }

   public int getAmount() {
      return 1;
   }

   static {
      ITEMS = List.of(Items.SUSPICIOUS_SAND, Items.SUSPICIOUS_GRAVEL);
   }
}
