package com.draftoutmc.draftout.lockout.icon;

import com.google.gson.annotations.SerializedName;

public enum GoalIconAnimationMode {
   @SerializedName("sequential")
   SEQUENTIAL,
   @SerializedName("slot-group-random")
   SLOT_GROUP_RANDOM;

   // $FF: synthetic method
   private static GoalIconAnimationMode[] $values() {
      return new GoalIconAnimationMode[]{SEQUENTIAL, SLOT_GROUP_RANDOM};
   }
}
