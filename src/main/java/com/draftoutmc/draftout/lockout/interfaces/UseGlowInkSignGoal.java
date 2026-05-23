package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.world.item.Item;

public abstract class UseGlowInkSignGoal extends Goal implements TextureProvider {
   public UseGlowInkSignGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Item> getSignItems();
}
