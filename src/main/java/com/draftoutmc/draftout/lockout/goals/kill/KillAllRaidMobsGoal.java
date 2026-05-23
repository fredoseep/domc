package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.interfaces.KillAllSpecificMobsGoal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class KillAllRaidMobsGoal extends KillAllSpecificMobsGoal implements CustomTextureRenderer {
   private static final List<EntityType<?>> MOBS;
   private static final Identifier TEXTURE;

   public KillAllRaidMobsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Kill all Raid Mobs";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public List<EntityType<?>> getEntityTypes() {
      return MOBS;
   }

   public Map<LockoutTeam, LinkedHashSet<EntityType<?>>> getTrackerMap() {
      return LockoutMatchData.getLockout().killedRaidMobs;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<EntityType<?>> raidMobs = (LinkedHashSet)this.getTrackerMap().getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = raidMobs.size();
      tooltip.add("Raid mobs killed: " + var10001 + "/" + MOBS.size());
      tooltip.addAll(HasTooltipInfo.commaSeparatedList(raidMobs.stream().map((type) -> type.getDescription().getString()).toList()));
      tooltip.add(" ");
      return tooltip;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      Utility.drawStackCount(context, x, y, String.valueOf(MOBS.size()));
      return true;
   }

   static {
      MOBS = List.of(EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.RAVAGER, EntityType.WITCH, EntityType.VEX, EntityType.EVOKER);
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/status_effect/bad_omen.png");
   }
}
