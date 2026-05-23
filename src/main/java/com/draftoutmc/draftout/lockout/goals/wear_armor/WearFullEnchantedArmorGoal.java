package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.lockout.icon.GoalIconAnimationMode;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrameProvider;
import com.draftoutmc.draftout.lockout.interfaces.WearArmorGoal;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WearFullEnchantedArmorGoal extends WearArmorGoal implements GoalIconFrameProvider {
   private static final List<Item> HELMETS;
   private static final List<Item> CHESTPLATES;
   private static final List<Item> LEGGINGS;
   private static final List<Item> BOOTS;
   private static final List<List<Item>> ITEMS;
   private int lastTickArmorChanged = -1;
   private Item armorPiece;

   public WearFullEnchantedArmorGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear Full Enchanted Armor";
   }

   public List<Item> getItems() {
      return null;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      ArrayList<ItemStack> armor = new ArrayList();
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.HEAD));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.CHEST));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.LEGS));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.FEET));

      for(ItemStack itemStack : armor) {
         if (itemStack.isEmpty() || !itemStack.isEnchanted()) {
            return false;
         }
      }

      return true;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      int cycleTicks = LockoutConfig.getInstance().cycleSpeed / 50;
      List<Item> itemType = (List)ITEMS.get(tick % (cycleTicks * 4) / cycleTicks);
      int armorChange = tick / cycleTicks;
      if (this.lastTickArmorChanged != armorChange) {
         this.lastTickArmorChanged = armorChange;
         this.armorPiece = (Item)itemType.get(Lockout.random.nextInt(itemType.size()));
      }

      ItemStack stack = this.armorPiece.getDefaultInstance();
      stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      context.item(stack, x, y);
      return true;
   }

   public List<GoalIconFrame> getGoalIconFrames() {
      return ITEMS.stream().flatMap(Collection::stream).map((item) -> GoalIconFrame.item(item, true)).toList();
   }

   public GoalIconAnimationMode getGoalIconAnimationMode() {
      return GoalIconAnimationMode.SLOT_GROUP_RANDOM;
   }

   static {
      HELMETS = List.of(Items.LEATHER_HELMET, Items.COPPER_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET);
      CHESTPLATES = List.of(Items.LEATHER_CHESTPLATE, Items.COPPER_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
      LEGGINGS = List.of(Items.LEATHER_LEGGINGS, Items.COPPER_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS);
      BOOTS = List.of(Items.LEATHER_BOOTS, Items.COPPER_BOOTS, Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS);
      ITEMS = List.of(HELMETS, CHESTPLATES, LEGGINGS, BOOTS);
   }
}
