package com.draftoutmc.draftout.lockout.icon;

import java.util.List;
import org.jspecify.annotations.Nullable;

public interface GoalIconFrameProvider {
   List<GoalIconFrame> getGoalIconFrames();

   default @Nullable Integer getGoalIconCycleMs() {
      return null;
   }

   default GoalIconAnimationMode getGoalIconAnimationMode() {
      return GoalIconAnimationMode.SEQUENTIAL;
   }
}
