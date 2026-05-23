package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.world.entity.EntityType;

public abstract class KillSpecificMobsGoal extends Goal implements RequiresAmount, Trackable<LockoutTeam, Integer>, HasTooltipInfo {
   public KillSpecificMobsGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<EntityType<?>> getEntityTypes();
}
