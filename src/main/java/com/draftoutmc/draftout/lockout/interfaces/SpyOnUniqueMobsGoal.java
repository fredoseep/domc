package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class SpyOnUniqueMobsGoal extends Goal implements RequiresAmount, Trackable<LockoutTeam, LinkedHashSet<EntityType<?>>>, HasTooltipInfo {
   private final ItemStack DISPLAY_ITEM_STACK;

   public SpyOnUniqueMobsGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.SPYGLASS.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.setCount(this.getAmount());
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }

   public String getGoalName() {
      return String.format("Spy on %d Unique Mobs", this.getAmount());
   }

   public Map<LockoutTeam, LinkedHashSet<EntityType<?>>> getTrackerMap() {
      return LockoutMatchData.getLockout().spiedOnMobs;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<EntityType<?>> mobs = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = mobs.size();
      tooltip.add("Mobs spied: " + var10001 + "/" + this.getAmount());
      tooltip.addAll(HasTooltipInfo.commaSeparatedList(mobs.stream().map((type) -> type.getDescription().getString()).toList()));
      tooltip.add(" ");
      return tooltip;
   }
}
