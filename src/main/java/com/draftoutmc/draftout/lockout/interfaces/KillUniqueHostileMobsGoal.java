package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public abstract class KillUniqueHostileMobsGoal extends Goal implements RequiresAmount, HasTooltipInfo {
   public KillUniqueHostileMobsGoal(String id, String data) {
      super(id, data);
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<EntityType<?>> hostiles = (LinkedHashSet)LockoutMatchData.getLockout().killedHostileTypes.getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = ((LinkedHashSet)LockoutMatchData.getLockout().killedHostileTypes.getOrDefault(team, new LinkedHashSet())).size();
      tooltip.add("Unique Hostile Mobs Killed: " + var10001 + "/" + this.getAmount());
      tooltip.addAll(HasTooltipInfo.commaSeparatedList(hostiles.stream().map((type) -> type.getDescription().getString()).toList()));
      tooltip.add(" ");
      return tooltip;
   }
}
