package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.resources.Identifier;

public abstract class AdvancementGoal extends Goal {
   public AdvancementGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Identifier> getAdvancements();
}
