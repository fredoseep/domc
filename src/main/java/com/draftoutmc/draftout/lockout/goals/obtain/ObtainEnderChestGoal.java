package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainEnderChestGoal extends ObtainAllItemsGoal {
   private static final List<Item> ITEMS;

   public ObtainEnderChestGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain Ender Chest";
   }

   static {
      ITEMS = List.of(Items.ENDER_CHEST);
   }
}
