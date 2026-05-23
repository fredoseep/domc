package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CycleItemTexturesProvider;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class ConsumeItemsGoal extends Goal implements CycleItemTexturesProvider, Trackable<LockoutTeam, LinkedHashSet<Item>>, HasTooltipInfo {
   public ConsumeItemsGoal(String id, String data) {
      super(id, data);
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public abstract List<Item> getItems();

   public List<Item> getItemsToDisplay() {
      return this.getItems();
   }

   public Map<LockoutTeam, LinkedHashSet<Item>> getTrackerMap() {
      return LockoutMatchData.getLockout().foodTypesEaten;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<Item> consumed = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      Sets.SetView<Item> unconsumed = Sets.difference(new LinkedHashSet(this.getItems()), consumed);
      Sets.SetView<Item> consumedItems = Sets.intersection(new LinkedHashSet(this.getItems()), consumed);
      tooltip.add(" ");
      int var10001 = consumedItems.size();
      tooltip.add("Consumed: " + var10001 + "/" + this.getItems().size());
      unconsumed.stream().iterator().forEachRemaining((item) -> {
         String colorFormat = String.valueOf(ChatFormatting.GRAY);
         tooltip.add(" " + colorFormat + "☐ " + Component.translatable(item.getDescriptionId()).getString());
      });
      consumedItems.stream().iterator().forEachRemaining((item) -> {
         String colorFormat = String.valueOf(ChatFormatting.GREEN);
         tooltip.add(" " + colorFormat + "☑ " + Component.translatable(item.getDescriptionId()).getString());
      });
      tooltip.add(" ");
      return tooltip;
   }
}
