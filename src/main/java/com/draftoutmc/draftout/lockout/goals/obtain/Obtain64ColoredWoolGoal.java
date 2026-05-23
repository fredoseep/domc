package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import com.draftoutmc.draftout.lockout.interfaces.RequiresAmount;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Obtain64ColoredWoolGoal extends ObtainAllItemsGoal implements RequiresAmount {
   private final ItemStack ITEM_STACK;
   private final List<Item> ITEMS;
   private final String GOAL_NAME;

   public Obtain64ColoredWoolGoal(String id, String data) {
      super(id, data);
      DyeColor DYE_COLOR = GoalDataConstants.getDyeColor(data);
      this.GOAL_NAME = "Obtain 64 " + GoalDataConstants.getDyeColorFormatted(DYE_COLOR) + " Wool";
      this.ITEMS = List.of(getWoolColor(data));
      this.ITEM_STACK = getWoolColor(data).getDefaultInstance();
      this.ITEM_STACK.setCount(64);
   }

   public int getAmount() {
      return 64;
   }

   public List<Item> getItems() {
      return this.ITEMS;
   }

   public String getGoalName() {
      return this.GOAL_NAME;
   }

   public static Item getWoolColor(String colorString) {
      Item var10000;
      switch (colorString) {
         case "white" -> var10000 = Items.WHITE_WOOL;
         case "orange" -> var10000 = Items.ORANGE_WOOL;
         case "magenta" -> var10000 = Items.MAGENTA_WOOL;
         case "light_blue" -> var10000 = Items.LIGHT_BLUE_WOOL;
         case "yellow" -> var10000 = Items.YELLOW_WOOL;
         case "lime" -> var10000 = Items.LIME_WOOL;
         case "pink" -> var10000 = Items.PINK_WOOL;
         case "gray" -> var10000 = Items.GRAY_WOOL;
         case "light_gray" -> var10000 = Items.LIGHT_GRAY_WOOL;
         case "cyan" -> var10000 = Items.CYAN_WOOL;
         case "purple" -> var10000 = Items.PURPLE_WOOL;
         case "blue" -> var10000 = Items.BLUE_WOOL;
         case "brown" -> var10000 = Items.BROWN_WOOL;
         case "green" -> var10000 = Items.GREEN_WOOL;
         case "red" -> var10000 = Items.RED_WOOL;
         case "black" -> var10000 = Items.BLACK_WOOL;
         default -> var10000 = null;
      }

      return var10000;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      super.renderTexture(context, x, y, tick);
      context.itemDecorations(Minecraft.getInstance().font, this.ITEM_STACK, x, y);
      return true;
   }
}
