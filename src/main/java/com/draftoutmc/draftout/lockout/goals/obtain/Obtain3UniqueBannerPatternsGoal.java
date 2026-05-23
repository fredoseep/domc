package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain3UniqueBannerPatternsGoal extends ObtainSomeOfTheItemsGoal {
   private static final List<Item> ITEMS;

   public Obtain3UniqueBannerPatternsGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 3 Unique Banner Patterns";
   }

   public int getAmount() {
      return 3;
   }

   static {
      ITEMS = List.of(Items.BORDURE_INDENTED_BANNER_PATTERN, Items.CREEPER_BANNER_PATTERN, Items.FIELD_MASONED_BANNER_PATTERN, Items.FLOW_BANNER_PATTERN, Items.FLOWER_BANNER_PATTERN, Items.GLOBE_BANNER_PATTERN, Items.GUSTER_BANNER_PATTERN, Items.MOJANG_BANNER_PATTERN, Items.PIGLIN_BANNER_PATTERN, Items.SKULL_BANNER_PATTERN);
   }
}
