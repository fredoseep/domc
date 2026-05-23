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

public class Kill30UndeadMobsGoal extends KillSpecificMobsGoal implements CycleTexturesProvider {
   private final ItemStack ITEM_STACK;
   private static final List<Identifier> TEXTURES = List.of(Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_zombie.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_wither_skeleton.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_zombie_villager.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_drowned.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_husk.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_stray.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/undead/kill_zoglin.png"));
   private static final List<EntityType<?>> UNDEAD_MOBS;

   public Kill30UndeadMobsGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.WOODEN_SWORD.getDefaultInstance();
      this.ITEM_STACK.setCount(30);
   }

   public String getGoalName() {
      return "Kill 30 Undead Mobs";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<EntityType<?>> getEntityTypes() {
      return UNDEAD_MOBS;
   }

   public int getAmount() {
      return 30;
   }

   public Map<LockoutTeam, Integer> getTrackerMap() {
      return LockoutMatchData.getLockout().killedUndeadMobs;
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
      tooltip.add("Undead Mobs killed: " + var10001 + "/" + this.getAmount());
      tooltip.add(" ");
      return tooltip;
   }

   static {
      UNDEAD_MOBS = List.of(EntityType.DROWNED, EntityType.HUSK, EntityType.PHANTOM, EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.STRAY, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN, EntityType.BOGGED);
   }
}
