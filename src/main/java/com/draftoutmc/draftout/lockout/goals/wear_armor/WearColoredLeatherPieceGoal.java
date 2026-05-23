package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import com.draftoutmc.draftout.lockout.interfaces.WearArmorPieceGoal;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jspecify.annotations.Nullable;

public class WearColoredLeatherPieceGoal extends WearArmorPieceGoal {
   private final Item ITEM;
   private final ItemStack DISPLAY_ITEM_STACK;
   private final int COLOR;
   private final String GOAL_NAME;

   public WearColoredLeatherPieceGoal(String id, String data) {
      super(id, data);
      String[] parts = data.split("&");
      this.ITEM = GoalDataConstants.getLeatherArmor(parts[0]);
      DyeColor DYE_COLOR = GoalDataConstants.getDyeColor(parts[1]);
      this.COLOR = GoalDataConstants.getDyeColorValue(DYE_COLOR);
      this.DISPLAY_ITEM_STACK = this.ITEM.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.set(DataComponents.DYED_COLOR, new DyedItemColor(this.COLOR));
      String var10001 = GoalDataConstants.getDyeColorFormatted(DYE_COLOR);
      this.GOAL_NAME = "Wear " + var10001 + " " + GoalDataConstants.getArmorPieceFormatted(parts[0]);
   }

   public String getGoalName() {
      return this.GOAL_NAME;
   }

   public List<Item> getItems() {
      return List.of(this.ITEM);
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      return false;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      ArrayList<ItemStack> armor = new ArrayList();
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.HEAD));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.CHEST));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.LEGS));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.FEET));

      for(ItemStack item : armor) {
         if (item != null && item.getItem().equals(this.ITEM) && (Boolean)Optional.ofNullable((DyedItemColor)item.get(DataComponents.DYED_COLOR)).map((dyed) -> dyed.rgb() == this.COLOR).orElse(false)) {
            return true;
         }
      }

      return false;
   }
}
