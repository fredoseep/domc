package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.entity.EntityType;

public abstract class KillMobGoal extends Goal {
   public KillMobGoal(String id, String data) {
      super(id, data);
   }

   public abstract EntityType<?> getEntity();
}
