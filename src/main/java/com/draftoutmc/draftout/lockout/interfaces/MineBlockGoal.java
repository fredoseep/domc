package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.world.item.Item;

public abstract class MineBlockGoal extends Goal {
   public MineBlockGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Item> getItems();
}
