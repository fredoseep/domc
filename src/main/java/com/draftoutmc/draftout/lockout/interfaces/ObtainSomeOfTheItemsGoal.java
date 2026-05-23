package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ObtainSomeOfTheItemsGoal extends ObtainItemsGoal implements RequiresAmount {
   public ObtainSomeOfTheItemsGoal(String id, String data) {
      super(id, data);
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      List<Item> items = new ArrayList(this.getItems());
      int count = 0;

      for(ItemStack item : ((PlayerInventoryAccessor)playerInventory).getItems()) {
         if (item != null && items.remove(item.getItem())) {
            ++count;
            if (count >= this.getAmount()) {
               return true;
            }
         }
      }

      for(EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
         ItemStack item = ((PlayerInventoryAccessor)playerInventory).getEquipment().get(equipmentSlot);
         if (items.remove(item.getItem())) {
            ++count;
            if (count >= this.getAmount()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      super.renderTexture(context, x, y, tick);
      if (this.getAmount() > 1) {
         Utility.drawStackCount(context, x, y, String.valueOf(this.getAmount()));
      }

      return true;
   }
}
