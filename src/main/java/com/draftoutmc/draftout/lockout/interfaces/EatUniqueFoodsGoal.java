package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class EatUniqueFoodsGoal extends Goal implements RequiresAmount, Trackable<LockoutTeam, LinkedHashSet<Item>>, CustomTextureRenderer, HasTooltipInfo {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/eat_unique.png");
   private final ItemStack DISPLAY_ITEM_STACK;

   public EatUniqueFoodsGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.APPLE.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.setCount(this.getAmount());
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.DISPLAY_ITEM_STACK;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      context.itemDecorations(Minecraft.getInstance().font, this.DISPLAY_ITEM_STACK, x, y);
      return true;
   }

   public Map<LockoutTeam, LinkedHashSet<Item>> getTrackerMap() {
      return LockoutMatchData.getLockout().foodTypesEaten;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<Item> foods = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = foods.size();
      tooltip.add("Unique foods eaten: " + var10001 + "/" + this.getAmount());
      if (foods.isEmpty()) {
         tooltip.add(" ");
      }

      return tooltip;
   }

   public List<ItemStack> getItems(LockoutTeam team, Player player) {
      LinkedHashSet<Item> trackedItems = (LinkedHashSet<Item>) this.getTrackerMap().getOrDefault(team, new LinkedHashSet<>());
      return trackedItems.stream().map(Item::getDefaultInstance).toList();   }
}
