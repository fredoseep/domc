package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.interfaces.BreakItemGoal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BreakArmorPieceGoal extends BreakItemGoal {
   private static final List<Item> HELMETS;
   private static final List<Item> CHESTPLATES;
   private static final List<Item> LEGGINGS;
   private static final List<Item> BOOTS;
   private static final List<Item> ITEMS;
   private static final Identifier TEXTURE;

   public BreakArmorPieceGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Break any Armor Piece";
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   static {
      HELMETS = List.of(Items.LEATHER_HELMET, Items.COPPER_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET);
      CHESTPLATES = List.of(Items.LEATHER_CHESTPLATE, Items.COPPER_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
      LEGGINGS = List.of(Items.LEATHER_LEGGINGS, Items.COPPER_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS);
      BOOTS = List.of(Items.LEATHER_BOOTS, Items.COPPER_BOOTS, Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS);
      ITEMS = (List)Stream.of(HELMETS, CHESTPLATES, LEGGINGS, BOOTS).flatMap(Collection::stream).collect(Collectors.toList());
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/break/break_armor.png");
   }
}
