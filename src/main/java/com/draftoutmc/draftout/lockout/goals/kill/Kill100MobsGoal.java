package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.interfaces.RequiresAmount;
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

public class Kill100MobsGoal extends Goal implements TextureProvider, CustomTextureRenderer, HasTooltipInfo, RequiresAmount {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill/kill_100_mobs.png");

   public Kill100MobsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Kill " + this.getAmount() + " mobs";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      Utility.drawStackCount(context, x, y, String.valueOf(this.getAmount()));
      return true;
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      tooltip.add(" ");
      String var10001 = String.valueOf(LockoutMatchData.getLockout().mobsKilled.getOrDefault(team, 0));
      tooltip.add("Mobs killed: " + var10001 + "/" + this.getAmount());
      tooltip.add(" ");
      return tooltip;
   }

   public int getAmount() {
      return 100;
   }
}
