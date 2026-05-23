package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class WearArmorGoal extends ObtainAllItemsGoal {
   public WearArmorGoal(String id, String data) {
      super(id, data);
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      List<Item> items = new ArrayList(this.getItems());
      ArrayList<ItemStack> armor = new ArrayList();
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.HEAD));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.CHEST));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.LEGS));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.FEET));

      for(ItemStack item : armor) {
         if (item != null && items.remove(item.getItem()) && items.isEmpty()) {
            return true;
         }
      }

      return false;
   }
}
