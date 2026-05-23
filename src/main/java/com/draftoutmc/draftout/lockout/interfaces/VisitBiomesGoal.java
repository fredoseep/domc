package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public abstract class VisitBiomesGoal extends Goal implements Trackable<LockoutTeam, LinkedHashSet<Identifier>>, HasTooltipInfo {
   public VisitBiomesGoal(String id, String data) {
      super(id, data);
   }

   public abstract List<Identifier> getBiomes();

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<Identifier> visited = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      Sets.SetView<Identifier> unvisited = Sets.difference(new LinkedHashSet(this.getBiomes()), visited);
      tooltip.add(" ");
      int var10001 = visited.size();
      tooltip.add("Biomes: " + var10001 + "/" + this.getBiomes().size());
      unvisited.stream().iterator().forEachRemaining((id) -> {
         String colorFormat = String.valueOf(ChatFormatting.GRAY);
         tooltip.add(" " + colorFormat + "☐ " + Component.translatable("biome.minecraft." + id.toShortString()).getString());
      });
      visited.stream().iterator().forEachRemaining((id) -> {
         String colorFormat = String.valueOf(ChatFormatting.GREEN);
         tooltip.add(" " + colorFormat + "☑ " + Component.translatable("biome.minecraft." + id.toShortString()).getString());
      });
      tooltip.add(" ");
      return tooltip;
   }
}
