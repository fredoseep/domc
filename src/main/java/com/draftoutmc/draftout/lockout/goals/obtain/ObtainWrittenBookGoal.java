package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrameProvider;
import com.draftoutmc.draftout.lockout.interfaces.ObtainAllItemsGoal;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ObtainWrittenBookGoal extends ObtainAllItemsGoal implements GoalIconFrameProvider {
   private static final List<Item> ITEMS;

   public ObtainWrittenBookGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Write a Book";
   }

   public List<GoalIconFrame> getGoalIconFrames() {
      return List.of(GoalIconFrame.item(Items.WRITTEN_BOOK, true));
   }

   static {
      ITEMS = List.of(Items.WRITTEN_BOOK);
   }
}
