package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import com.draftoutmc.draftout.lockout.interfaces.RequiresAmount;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain64CoarseDirtGoal extends ObtainAllItemsGoal implements RequiresAmount {
   private static final List<Item> ITEMS;

   public Obtain64CoarseDirtGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 64 Coarse Dirt";
   }

   public int getAmount() {
      return 64;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      super.renderTexture(context, x, y, tick);
      Utility.drawStackCount(context, x, y, String.valueOf(this.getAmount()));
      return true;
   }

   static {
      ITEMS = List.of(Items.COARSE_DIRT);
   }
}
