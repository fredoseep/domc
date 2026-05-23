package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.world.entity.EntityType;

public abstract class NametagMobGoal extends Goal implements TextureProvider {
   public NametagMobGoal(String id, String data) {
      super(id, data);
   }

   public abstract EntityType<?> getEntity();

   public abstract List<String> getAcceptableNames();
}
