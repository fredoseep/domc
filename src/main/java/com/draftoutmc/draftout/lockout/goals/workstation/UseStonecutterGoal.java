package com.draftoutmc.draftout.lockout.goals.workstation;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrameProvider;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseStonecutterGoal extends Goal implements GoalIconFrameProvider {
   private final ItemStack ITEM_STACK;

   public UseStonecutterGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.STONECUTTER.getDefaultInstance();
   }

   public String getGoalName() {
      return "Use Stonecutter";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<GoalIconFrame> getGoalIconFrames() {
      return List.of(GoalIconFrame.animatedGoalTicks(this, 50, 0, 1, 2));
   }
}
