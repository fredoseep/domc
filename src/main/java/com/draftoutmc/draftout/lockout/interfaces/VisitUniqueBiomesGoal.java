package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import java.util.LinkedHashSet;
import net.minecraft.world.item.Item;

public abstract class VisitUniqueBiomesGoal extends Goal implements RequiresAmount, Trackable<LockoutTeam, LinkedHashSet<Item>>, CustomTextureRenderer, HasTooltipInfo {
   public VisitUniqueBiomesGoal(String id, String data) {
      super(id, data);
   }
}
