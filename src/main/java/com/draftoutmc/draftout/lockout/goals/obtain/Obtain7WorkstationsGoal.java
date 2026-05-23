package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrameProvider;
import com.draftoutmc.draftout.lockout.interfaces.ObtainSomeOfTheItemsGoal;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Obtain7WorkstationsGoal extends ObtainSomeOfTheItemsGoal implements GoalIconFrameProvider {
   private static final List<Item> ITEMS;

   public Obtain7WorkstationsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 7;
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Obtain 7 Unique Workstations";
   }

   public List<GoalIconFrame> getGoalIconFrames() {
      List<GoalIconFrame> frames = new ArrayList(ITEMS.size());

      for(int i = 0; i < ITEMS.size(); ++i) {
         int baseTick = i * 60;
         if (ITEMS.get(i) == Items.STONECUTTER) {
            frames.add(GoalIconFrame.animatedGoalTicks(this, 50, baseTick, baseTick + 1, baseTick + 2));
         } else {
            frames.add(GoalIconFrame.renderGoalAtTick(this, baseTick));
         }
      }

      return frames;
   }

   static {
      ITEMS = List.of(Items.BLAST_FURNACE, Items.SMOKER, Items.CARTOGRAPHY_TABLE, Items.BREWING_STAND, Items.BARREL, Items.COMPOSTER, Items.FLETCHING_TABLE, Items.CAULDRON, Items.LECTERN, Items.STONECUTTER, Items.LOOM, Items.SMITHING_TABLE, Items.GRINDSTONE);
   }
}
