package com.draftoutmc.draftout.lockout.goals.status_effect;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.RequiresAmount;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;

public abstract class GetXStatusEffectsGoal extends Goal implements RequiresAmount, TextureProvider {
   public GetXStatusEffectsGoal(String id, String data) {
      super(id, data);
   }
}
