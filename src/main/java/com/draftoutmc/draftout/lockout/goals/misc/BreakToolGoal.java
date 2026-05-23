package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.goals.obtain.ObtainCopperToolsGoal;
import com.draftoutmc.draftout.lockout.goals.obtain.ObtainDiamondToolsGoal;
import com.draftoutmc.draftout.lockout.goals.obtain.ObtainGoldenToolsGoal;
import com.draftoutmc.draftout.lockout.goals.obtain.ObtainIronToolsGoal;
import com.draftoutmc.draftout.lockout.goals.obtain.ObtainStoneToolsGoal;
import com.draftoutmc.draftout.lockout.goals.obtain.ObtainWoodenToolsGoal;
import com.draftoutmc.draftout.lockout.interfaces.BreakItemGoal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BreakToolGoal extends BreakItemGoal {
   private static final List<Item> MISC;
   private static final List<Item> ITEMS;
   private static final Identifier TEXTURE;

   public BreakToolGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Break any Tool";
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   static {
      MISC = List.of(Items.FISHING_ROD, Items.FLINT_AND_STEEL, Items.SHEARS, Items.BRUSH, Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK);
      ITEMS = (List)Stream.of(ObtainCopperToolsGoal.ITEMS, ObtainWoodenToolsGoal.ITEMS, ObtainDiamondToolsGoal.ITEMS, ObtainGoldenToolsGoal.ITEMS, ObtainIronToolsGoal.ITEMS, ObtainStoneToolsGoal.ITEMS, MISC).flatMap(Collection::stream).collect(Collectors.toList());
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/break/break_tool.png");
   }
}
