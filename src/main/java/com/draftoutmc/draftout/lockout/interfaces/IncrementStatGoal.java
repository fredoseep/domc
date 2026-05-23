package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.resources.Identifier;

public abstract class IncrementStatGoal extends Goal {
   public IncrementStatGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Identifier> getStats();
}
