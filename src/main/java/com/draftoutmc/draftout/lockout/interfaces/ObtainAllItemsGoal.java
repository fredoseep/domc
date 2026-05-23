package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ObtainAllItemsGoal extends ObtainItemsGoal {
   public ObtainAllItemsGoal(String id, String data) {
      super(id, data);
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      List<Item> items = new ArrayList(this.getItems());

      for(EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
         ItemStack item = ((PlayerInventoryAccessor)playerInventory).getEquipment().get(equipmentSlot);
         if (item != null && this.checkRequiredAmount(item, playerInventory, items)) {
            return true;
         }
      }

      for(ItemStack item : ((PlayerInventoryAccessor)playerInventory).getItems()) {
         if (item != null && this.checkRequiredAmount(item, playerInventory, items)) {
            return true;
         }
      }

      return false;
   }

   private boolean checkRequiredAmount(ItemStack item, Inventory playerInventory, List<Item> items) {
      boolean allow = true;
      if (this instanceof RequiresAmount requiresAmount) {
         allow = playerInventory.countItem(item.getItem()) >= requiresAmount.getAmount();
      }

      return allow && items.remove(item.getItem()) ? items.isEmpty() : false;
   }
}
