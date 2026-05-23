package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.world.entity.EntityType;

public abstract class KillAllSpecificMobsGoal extends Goal implements Trackable<LockoutTeam, LinkedHashSet<EntityType<?>>>, HasTooltipInfo {
   public KillAllSpecificMobsGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<EntityType<?>> getEntityTypes();
}
