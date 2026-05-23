package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.server.LockoutServer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class GetUniqueAdvancementsGoal extends Goal implements RequiresAmount, Trackable<LockoutTeam, LinkedHashSet<Identifier>>, TextureProvider, HasTooltipInfo {
   private final ItemStack DISPLAY_ITEM_STACK;

   public GetUniqueAdvancementsGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.APPLE.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.setCount(this.getAmount());
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }

   public Map<LockoutTeam, LinkedHashSet<Identifier>> getTrackerMap() {
      return LockoutMatchData.getLockout().uniqueAdvancements;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<Identifier> advancements = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = advancements.size();
      tooltip.add("Advancements: " + var10001 + "/" + this.getAmount());
      tooltip.addAll(HasTooltipInfo.commaSeparatedList(advancements.stream().map((id) -> ((DisplayInfo)LockoutServer.server.getAdvancements().get(id).value().display().get()).getTitle().getString()).toList()));
      tooltip.add(" ");
      return tooltip;
   }
}
