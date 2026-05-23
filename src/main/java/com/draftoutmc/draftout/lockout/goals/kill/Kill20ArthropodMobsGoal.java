package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.interfaces.KillSpecificMobsGoal;
import com.draftoutmc.draftout.lockout.texture.CycleTexturesProvider;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class Kill20ArthropodMobsGoal extends KillSpecificMobsGoal implements CycleTexturesProvider {
   private final ItemStack ITEM_STACK;
   private static final List<Identifier> TEXTURES = List.of(Identifier.fromNamespaceAndPath("draftout", "textures/custom/arthropod/kill_spider.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/arthropod/kill_bee.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/arthropod/kill_cave_spider.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/arthropod/kill_endermite.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/arthropod/kill_silverfish.png"));
   private static final List<EntityType<?>> ARTHROPOD_MOBS;

   public Kill20ArthropodMobsGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.WOODEN_SWORD.getDefaultInstance();
      this.ITEM_STACK.setCount(20);
   }

   public String getGoalName() {
      return "Kill 20 Arthropods";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<EntityType<?>> getEntityTypes() {
      return ARTHROPOD_MOBS;
   }

   public int getAmount() {
      return 20;
   }

   public Map<LockoutTeam, Integer> getTrackerMap() {
      return LockoutMatchData.getLockout().killedArthropods;
   }

   public List<Identifier> getTexturesToDisplay() {
      return TEXTURES;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      CycleTexturesProvider.super.renderTexture(context, x, y, tick);
      context.itemDecorations(Minecraft.getInstance().font, this.ITEM_STACK, x, y);
      return true;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      tooltip.add(" ");
      String var10001 = String.valueOf(this.getTrackerMap().getOrDefault(team, 0));
      tooltip.add("Arthropods killed: " + var10001 + "/" + this.getAmount());
      tooltip.add(" ");
      return tooltip;
   }

   static {
      ARTHROPOD_MOBS = List.of(EntityType.BEE, EntityType.CAVE_SPIDER, EntityType.SPIDER, EntityType.ENDERMITE, EntityType.SILVERFISH);
   }
}
