package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public abstract class BreedUniqueAnimalsGoal extends Goal implements RequiresAmount, CustomTextureRenderer, HasTooltipInfo {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/breed/breed_x.png");
   private final ItemStack DISPLAY_ITEM_STACK;

   public BreedUniqueAnimalsGoal(String id, String data) {
      super(id, data);
      this.DISPLAY_ITEM_STACK = Items.WHEAT.getDefaultInstance();
      this.DISPLAY_ITEM_STACK.setCount(this.getAmount());
   }

   public String getGoalName() {
      return String.format("Breed %d Unique Animals", this.getAmount());
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      LinkedHashSet<EntityType<?>> animals = (LinkedHashSet)LockoutMatchData.getLockout().bredAnimalTypes.getOrDefault(team, new LinkedHashSet());
      tooltip.add(" ");
      int var10001 = animals.size();
      tooltip.add("Animals bred: " + var10001 + "/" + this.getAmount());
      tooltip.addAll(HasTooltipInfo.commaSeparatedList(animals.stream().map((type) -> type.getDescription().getString()).toList()));
      tooltip.add(" ");
      return tooltip;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      context.itemDecorations(Minecraft.getInstance().font, this.DISPLAY_ITEM_STACK, x, y);
      return true;
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }
}
