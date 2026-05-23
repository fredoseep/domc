package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrameProvider;
import com.draftoutmc.draftout.lockout.interfaces.WearArmorGoal;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;

public class WearUniqueColoredLeatherArmorGoal extends WearArmorGoal implements GoalIconFrameProvider {
   private static final List<Item> ITEMS;
   private int lastTickColorChanged = -1;
   private int color = 0;

   public WearUniqueColoredLeatherArmorGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear Full Leather Armor, in different colors";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      if (!super.satisfiedBy(playerInventory)) {
         return false;
      } else {
         ArrayList<ItemStack> armor = new ArrayList();
         armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.HEAD));
         armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.CHEST));
         armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.LEGS));
         armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.FEET));
         Set<Integer> colors = new HashSet();

         for(ItemStack itemStack : armor) {
            DyedItemColor dyedColor = (DyedItemColor)itemStack.get(DataComponents.DYED_COLOR);
            if (dyedColor != null) {
               int color = dyedColor.rgb();
               if (color != -6265536) {
                  colors.add(color);
               }
            }
         }

         return colors.size() == 4;
      }
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      int cycleTicks = LockoutConfig.getInstance().cycleSpeed / 50;
      int mod = tick % (cycleTicks * this.getItemsToDisplay().size());
      ItemStack itemStack = ((Item)this.getItemsToDisplay().get(mod / cycleTicks)).getDefaultInstance();
      int colorChange = tick / cycleTicks;
      if (this.lastTickColorChanged != colorChange) {
         this.lastTickColorChanged = colorChange;
         this.color = Lockout.random.nextInt(0, 256) << 16 | Lockout.random.nextInt(0, 256) << 8 | Lockout.random.nextInt(0, 256);
      }

      itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(this.color));
      context.item(itemStack, x, y);
      return true;
   }

   public List<GoalIconFrame> getGoalIconFrames() {
      return List.of(GoalIconFrame.dyedItem(Items.LEATHER_HELMET, GoalDataConstants.getDyeColorValue(DyeColor.RED)), GoalIconFrame.dyedItem(Items.LEATHER_CHESTPLATE, GoalDataConstants.getDyeColorValue(DyeColor.BLUE)), GoalIconFrame.dyedItem(Items.LEATHER_LEGGINGS, GoalDataConstants.getDyeColorValue(DyeColor.GREEN)), GoalIconFrame.dyedItem(Items.LEATHER_BOOTS, GoalDataConstants.getDyeColorValue(DyeColor.YELLOW)));
   }

   static {
      ITEMS = List.of(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
   }
}
