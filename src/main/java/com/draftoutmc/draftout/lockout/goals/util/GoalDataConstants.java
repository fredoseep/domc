package com.draftoutmc.draftout.lockout.goals.util;

import java.util.List;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;

public class GoalDataConstants {
   public static final String DATA_NONE = "null";
   public static final String DATA_SEPARATOR = "&";
   public static final String DATA_LEATHER_CHESTPLATE = "leather_chestplate";
   public static final String DATA_LEATHER_HELMET = "leather_helmet";
   public static final String DATA_LEATHER_LEGGINGS = "leather_leggings";
   public static final String DATA_LEATHER_BOOTS = "leather_boots";
   public static final List<String> DATA_LEATHER_ARMOR = List.of("leather_helmet", "leather_chestplate", "leather_leggings", "leather_boots");
   public static final String DATA_COLOR_BLACK = "black";
   public static final String DATA_COLOR_WHITE = "white";
   public static final String DATA_COLOR_RED = "red";
   public static final String DATA_COLOR_YELLOW = "yellow";
   public static final String DATA_COLOR_PINK = "pink";
   public static final String DATA_COLOR_PURPLE = "purple";
   public static final String DATA_COLOR_LIME = "lime";
   public static final String DATA_COLOR_CYAN = "cyan";
   public static final String DATA_COLOR_LIGHT_GRAY = "light_gray";
   public static final String DATA_COLOR_GRAY = "gray";
   public static final String DATA_COLOR_GREEN = "green";
   public static final String DATA_COLOR_BLUE = "blue";
   public static final String DATA_COLOR_LIGHT_BLUE = "light_blue";
   public static final String DATA_COLOR_BROWN = "brown";
   public static final String DATA_COLOR_MAGENTA = "magenta";
   public static final String DATA_COLOR_ORANGE = "orange";

   public static Item getLeatherArmor(String leatherArmorString) {
      Item var10000;
      switch (leatherArmorString) {
         case "leather_helmet" -> var10000 = Items.LEATHER_HELMET;
         case "leather_chestplate" -> var10000 = Items.LEATHER_CHESTPLATE;
         case "leather_leggings" -> var10000 = Items.LEATHER_LEGGINGS;
         case "leather_boots" -> var10000 = Items.LEATHER_BOOTS;
         default -> var10000 = null;
      }

      return var10000;
   }

   public static DyeColor getDyeColor(String dyeColorString) {
      DyeColor var10000;
      switch (dyeColorString) {
         case "white" -> var10000 = DyeColor.WHITE;
         case "orange" -> var10000 = DyeColor.ORANGE;
         case "magenta" -> var10000 = DyeColor.MAGENTA;
         case "light_blue" -> var10000 = DyeColor.LIGHT_BLUE;
         case "yellow" -> var10000 = DyeColor.YELLOW;
         case "lime" -> var10000 = DyeColor.LIME;
         case "pink" -> var10000 = DyeColor.PINK;
         case "gray" -> var10000 = DyeColor.GRAY;
         case "light_gray" -> var10000 = DyeColor.LIGHT_GRAY;
         case "cyan" -> var10000 = DyeColor.CYAN;
         case "purple" -> var10000 = DyeColor.PURPLE;
         case "blue" -> var10000 = DyeColor.BLUE;
         case "brown" -> var10000 = DyeColor.BROWN;
         case "green" -> var10000 = DyeColor.GREEN;
         case "red" -> var10000 = DyeColor.RED;
         case "black" -> var10000 = DyeColor.BLACK;
         default -> var10000 = null;
      }

      return var10000;
   }

   public static String getDyeColorDataString(DyeColor dyeColor) {
      String var10000;
      switch (dyeColor) {
         case WHITE -> var10000 = "white";
         case ORANGE -> var10000 = "orange";
         case MAGENTA -> var10000 = "magenta";
         case LIGHT_BLUE -> var10000 = "light_blue";
         case YELLOW -> var10000 = "yellow";
         case LIME -> var10000 = "lime";
         case PINK -> var10000 = "pink";
         case GRAY -> var10000 = "gray";
         case LIGHT_GRAY -> var10000 = "light_gray";
         case CYAN -> var10000 = "cyan";
         case PURPLE -> var10000 = "purple";
         case BLUE -> var10000 = "blue";
         case BROWN -> var10000 = "brown";
         case GREEN -> var10000 = "green";
         case RED -> var10000 = "red";
         case BLACK -> var10000 = "black";
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static int getDyeColorValue(DyeColor dyeColor) {
      int var10000;
      switch (dyeColor) {
         case WHITE -> var10000 = 16383998;
         case ORANGE -> var10000 = 16351261;
         case MAGENTA -> var10000 = 13061821;
         case LIGHT_BLUE -> var10000 = 3847130;
         case YELLOW -> var10000 = 16701501;
         case LIME -> var10000 = 8439583;
         case PINK -> var10000 = 15961002;
         case GRAY -> var10000 = 4673362;
         case LIGHT_GRAY -> var10000 = 10329495;
         case CYAN -> var10000 = 1481884;
         case PURPLE -> var10000 = 8991416;
         case BLUE -> var10000 = 3949738;
         case BROWN -> var10000 = 8606770;
         case GREEN -> var10000 = 6192150;
         case RED -> var10000 = 11546150;
         case BLACK -> var10000 = 1908001;
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static String getDyeColorFormatted(DyeColor dyeColor) {
      return StringUtils.capitalize(dyeColor.toString().replace("_", " "));
   }

   public static String getArmorPieceFormatted(String leatherArmorString) {
      return StringUtils.capitalize(leatherArmorString.replace("_", " "));
   }
}
