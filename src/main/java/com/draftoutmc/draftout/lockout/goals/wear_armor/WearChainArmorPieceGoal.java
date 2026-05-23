package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.lockout.interfaces.WearArmorPieceGoal;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WearChainArmorPieceGoal extends WearArmorPieceGoal {
   private static final List<Item> ITEMS;

   public WearChainArmorPieceGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear a Chain Armor Piece";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      ArrayList<ItemStack> armor = new ArrayList();
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.HEAD));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.CHEST));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.LEGS));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.FEET));

      for(ItemStack item : armor) {
         if (item != null && ITEMS.contains(item.getItem())) {
            return true;
         }
      }

      return false;
   }

   static {
      ITEMS = List.of(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS);
   }
}
