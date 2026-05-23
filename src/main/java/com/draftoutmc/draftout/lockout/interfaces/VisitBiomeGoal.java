package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.resources.Identifier;

public abstract class VisitBiomeGoal extends Goal {
   public VisitBiomeGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Identifier> getBiomes();
}
