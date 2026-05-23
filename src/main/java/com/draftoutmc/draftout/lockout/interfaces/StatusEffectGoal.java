package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import net.minecraft.world.effect.MobEffect;

public abstract class StatusEffectGoal extends Goal {
   public StatusEffectGoal(String id, String data) {
      super(id, data);
   }

   public abstract MobEffect getStatusEffect();
}
