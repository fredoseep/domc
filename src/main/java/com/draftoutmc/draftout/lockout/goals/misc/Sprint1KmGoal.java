package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class Sprint1KmGoal extends Goal implements CustomTextureRenderer, HasTooltipInfo {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/sprint_1km.png");

   public Sprint1KmGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Sprint 1km";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      Utility.drawStackCount(context, x, y, "1km");
      return true;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      if (player == null) {
         return tooltip;
      } else {
         int distance = (Integer)LockoutMatchData.getLockout().distanceSprinted.getOrDefault(player.getUUID(), 0);
         tooltip.add(" ");
         tooltip.add("Sprinted: " + Math.min(1000, distance / 100) + "/1000m");
         tooltip.add(" ");
         return tooltip;
      }
   }
}
