package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.entity.EntityType;

public abstract class RideEntityGoal extends Goal {
   public RideEntityGoal(String id, String data) {
      super(id, data);
   }

   public abstract EntityType<?> getEntityType();
}
