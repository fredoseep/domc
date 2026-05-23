package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainColoredGlazedTerracottaGoal extends ObtainAllItemsGoal {
   private final List<Item> ITEMS;
   private final String GOAL_NAME;

   public ObtainColoredGlazedTerracottaGoal(String id, String data) {
      super(id, data);
      DyeColor DYE_COLOR = GoalDataConstants.getDyeColor(data);
      this.GOAL_NAME = "Obtain " + GoalDataConstants.getDyeColorFormatted(DYE_COLOR) + " Glazed Terracotta";
      this.ITEMS = List.of(getGlazedTerracottaColor(data));
   }

   public List<Item> getItems() {
      return this.ITEMS;
   }

   public String getGoalName() {
      return this.GOAL_NAME;
   }

   public static Item getGlazedTerracottaColor(String colorString) {
      Item var10000;
      switch (colorString) {
         case "white" -> var10000 = Items.WHITE_GLAZED_TERRACOTTA;
         case "orange" -> var10000 = Items.ORANGE_GLAZED_TERRACOTTA;
         case "magenta" -> var10000 = Items.MAGENTA_GLAZED_TERRACOTTA;
         case "light_blue" -> var10000 = Items.LIGHT_BLUE_GLAZED_TERRACOTTA;
         case "yellow" -> var10000 = Items.YELLOW_GLAZED_TERRACOTTA;
         case "lime" -> var10000 = Items.LIME_GLAZED_TERRACOTTA;
         case "pink" -> var10000 = Items.PINK_GLAZED_TERRACOTTA;
         case "gray" -> var10000 = Items.GRAY_GLAZED_TERRACOTTA;
         case "light_gray" -> var10000 = Items.LIGHT_GRAY_GLAZED_TERRACOTTA;
         case "cyan" -> var10000 = Items.CYAN_GLAZED_TERRACOTTA;
         case "purple" -> var10000 = Items.PURPLE_GLAZED_TERRACOTTA;
         case "blue" -> var10000 = Items.BLUE_GLAZED_TERRACOTTA;
         case "brown" -> var10000 = Items.BROWN_GLAZED_TERRACOTTA;
         case "green" -> var10000 = Items.GREEN_GLAZED_TERRACOTTA;
         case "red" -> var10000 = Items.RED_GLAZED_TERRACOTTA;
         case "black" -> var10000 = Items.BLACK_GLAZED_TERRACOTTA;
         default -> var10000 = null;
      }

      return var10000;
   }
}
