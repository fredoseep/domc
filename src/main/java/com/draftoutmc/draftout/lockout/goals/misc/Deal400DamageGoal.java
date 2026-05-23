package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class Deal400DamageGoal extends Goal implements TextureProvider, CustomTextureRenderer, HasTooltipInfo {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/deal_400_damage.png");

   public Deal400DamageGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Deal 400 damage";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      Utility.drawStackCount(context, x, y, "400");
      return true;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      double damage = (Double)LockoutMatchData.getLockout().damageDealt.getOrDefault(team, (double)0.0F);
      tooltip.add(" ");
      tooltip.add("Damage: " + Math.min(400, (int)damage) + "/400");
      tooltip.add(" ");
      return tooltip;
   }
}
