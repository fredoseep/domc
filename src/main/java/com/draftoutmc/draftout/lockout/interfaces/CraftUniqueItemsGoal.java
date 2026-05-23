package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class CraftUniqueItemsGoal extends Goal implements RequiresAmount, CustomTextureRenderer, HasTooltipInfo {
   private final ItemStack ITEM_STACK;

   public CraftUniqueItemsGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.CRAFTING_TABLE.getDefaultInstance();
   }

   public String getGoalName() {
      return String.format("Craft %d Unique Items", this.getAmount());
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      if (player == null) {
         return Collections.emptyList();
      } else {
         List<String> tooltip = new ArrayList();
         Set<Item> crafts = (Set)LockoutMatchData.getLockout().uniqueCrafts.getOrDefault(player.getUUID(), new LinkedHashSet());
         tooltip.add(" ");
         int var10001 = crafts.size();
         tooltip.add("Items crafted: " + var10001 + "/" + this.getAmount());
         tooltip.add(" ");
         return tooltip;
      }
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.item(this.ITEM_STACK, x, y);
      Utility.drawStackCount(context, x, y, String.valueOf(this.getAmount()));
      return true;
   }
}
