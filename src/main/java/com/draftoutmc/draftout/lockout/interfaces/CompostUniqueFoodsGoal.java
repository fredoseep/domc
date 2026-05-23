package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CycleTexturesProvider;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class CompostUniqueFoodsGoal extends Goal implements RequiresAmount, CycleTexturesProvider, HasTooltipInfo, Trackable<LockoutTeam, LinkedHashSet<Item>> {
   private static final List<Identifier> TEXTURES = List.of(Identifier.fromNamespaceAndPath("draftout", "textures/custom/compost/compost_1.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/compost/compost_2.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/compost/compost_3.png"));

   public CompostUniqueFoodsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return String.format("Compost %d Unique Foods", this.getAmount());
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<Item> foods = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = foods.size();
      tooltip.add("Foods composted: " + var10001 + "/" + this.getAmount());
      if (foods.isEmpty()) {
         tooltip.add(" ");
      }

      return tooltip;
   }

   public List<ItemStack> getItems(LockoutTeam team, Player player) {
      LinkedHashSet<Item> trackedItems = (LinkedHashSet<Item>) this.getTrackerMap().getOrDefault(team, new LinkedHashSet<>());
      return trackedItems.stream().map(Item::getDefaultInstance).toList();
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      CycleTexturesProvider.super.renderTexture(context, x, y, tick);
      Utility.drawStackCount(context, x, y, String.valueOf(this.getAmount()));
      return true;
   }

   public List<Identifier> getTexturesToDisplay() {
      return TEXTURES;
   }

   public Map<LockoutTeam, LinkedHashSet<Item>> getTrackerMap() {
      return LockoutMatchData.getLockout().foodTypesComposted;
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }
}
