package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.entity.EntityType;

public abstract class LeashMobGoal extends Goal {
   public LeashMobGoal(String id, String data) {
      super(id, data);
   }

   public abstract EntityType<?> getMob();
}
