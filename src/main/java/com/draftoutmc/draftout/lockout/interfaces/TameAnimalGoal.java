package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.world.entity.EntityType;

public abstract class TameAnimalGoal extends Goal {
   public TameAnimalGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<EntityType<?>> getAnimals();
}
