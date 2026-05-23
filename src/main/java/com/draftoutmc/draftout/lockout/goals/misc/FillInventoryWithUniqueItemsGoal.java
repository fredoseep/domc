package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.interfaces.ObtainItemsGoal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FillInventoryWithUniqueItemsGoal extends ObtainItemsGoal {
   private static final List<Item> ITEMS;

   public FillInventoryWithUniqueItemsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Fill Inventory with unique items";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      Set<Item> itemTypes = new HashSet();

      for(ItemStack item : playerInventory.getNonEquipmentItems()) {
         if (item == null || item.isEmpty()) {
            return false;
         }

         if (!itemTypes.add(item.getItem())) {
            return false;
         }
      }

      return itemTypes.size() == 36;
   }

   static {
      ITEMS = List.of(Items.CHEST);
   }
}
