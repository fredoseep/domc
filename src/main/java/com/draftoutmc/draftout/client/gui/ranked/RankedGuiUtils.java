package com.draftoutmc.draftout.client.gui.ranked;

import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.data.RankedRank;
import java.awt.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;

public class RankedGuiUtils {
   private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/gui/background/background.png");
   private static final Color GOLD = new Color(16766720);
   private static final Color SILVER = new Color(12632256);
   private static final Color BRONZE = new Color(13467442);
   private static final MutableComponent UNRANKED_COMPONENT;

   public static void extractBackground(GuiGraphicsExtractor graphics, int width, int height) {
      graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, 0, 0, 0.0F, 0.0F, width, height, 1920, 1080, 1920, 1080);
      graphics.nextStratum();
      graphics.blurBeforeThisStratum();
   }

   public static void scaledText(GuiGraphicsExtractor context, Component text, int x, int y, float scale, int color) {
      scaledText(context, text, x, y, scale, color, true);
   }

   public static void scaledText(GuiGraphicsExtractor context, Component text, int x, int y, float scale, int color, boolean dropShadow) {
      Matrix3x2fStack matrices = context.pose();
      matrices.pushMatrix();
      matrices.translate((float)x, (float)y);
      matrices.scale(scale, scale);
      context.text(Minecraft.getInstance().font, text, 0, 0, color, dropShadow);
      matrices.popMatrix();
   }

   public static void outlinedText(GuiGraphicsExtractor context, Component text, int x, int y, float scale, int textColor, int outlineColor) {
      Component emptyText = text.plainCopy();
      scaledText(context, emptyText, x - 1, y, scale, outlineColor, false);
      scaledText(context, emptyText, x + 1, y, scale, outlineColor, false);
      scaledText(context, emptyText, x, y - 1, scale, outlineColor, false);
      scaledText(context, emptyText, x, y + 1, scale, outlineColor, false);
      scaledText(context, text, x, y, scale, textColor, false);
   }

   public static void scaledCenteredText(GuiGraphicsExtractor context, Component text, int x, int y, float scale, int color) {
      Matrix3x2fStack matrices = context.pose();
      matrices.pushMatrix();
      matrices.scale(scale, scale);
      context.centeredText(Minecraft.getInstance().font, text, (int)((float)x / scale), (int)((float)y / scale), color);
      matrices.popMatrix();
   }

   public static void rightToLeftGradient(GuiGraphicsExtractor context, int x0, int y0, int x1, int y1, int rightColor, int leftColor) {
      int width = x1 - x0;
      int height = y1 - y0;
      context.pose().pushMatrix();
      context.pose().translate((float)x0, (float)y0);
      context.pose().rotate((float)Math.toRadians((double)90.0F));
      context.fillGradient(0, -width, height, 0, rightColor, leftColor);
      context.pose().popMatrix();
   }

   public static void blitScaled(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height, float scale) {
      float cx = (float)x + (float)width * scale / 2.0F;
      float cy = (float)y + (float)height * scale / 2.0F;
      graphics.pose().pushMatrix();
      graphics.pose().translate(cx, cy);
      graphics.pose().scale(scale, scale);
      graphics.blit(texture, (int)((float)(-width) / 2.0F), (int)((float)(-height) / 2.0F), 0, 0, (float)width, (float)height, (float)width, (float)height);
      graphics.pose().popMatrix();
   }

   public static void blitSpriteScaled(GuiGraphicsExtractor graphics, Identifier sprite, int x, int y, int width, int height, float scale) {
      graphics.pose().pushMatrix();
      graphics.pose().translate((float)x, (float)y);
      graphics.pose().scale(scale, scale);
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, width, height);
      graphics.pose().popMatrix();
   }

   public static Component getPlaceComponent(int place) {
      MutableComponent rankComponent = Component.literal("#" + place);
      if (place == 1) {
         rankComponent.withColor(-16777216 | GOLD.getRGB());
      }

      if (place == 2) {
         rankComponent.withColor(-16777216 | SILVER.getRGB());
      }

      if (place == 3) {
         rankComponent.withColor(-16777216 | BRONZE.getRGB());
      }

      return rankComponent;
   }

   public static Component getEloComponent(int elo) {
      return Component.literal(String.valueOf(elo)).withColor(RankedRank.getFromElo(elo).getColor());
   }

   public static Component getRankNameComponent(int elo) {
      RankedRank rank = RankedRank.getFromElo(elo);
      return Component.literal(rank.getDisplayName()).withColor(rank.getColor());
   }

   public static MutableComponent getEloAndRankNameComponent(boolean ranked, int elo) {
      return ranked ? Component.empty().append("[").append(getEloComponent(elo)).append("] ").append(getRankNameComponent(elo)) : UNRANKED_COMPONENT;
   }

   public static MutableComponent getEloAndRankNameComponent(MatchType matchType, boolean ranked, int elo) {
      return matchType == MatchType.COMPETITIVE ? getEloAndRankNameComponent(ranked, elo) : Component.empty();
   }

   public static MutableComponent getRankNameAndEloComponent(boolean ranked, int elo) {
      return ranked ? Component.empty().append(getRankNameComponent(elo)).append(" [").append(getEloComponent(elo)).append("]") : UNRANKED_COMPONENT;
   }

   public static MutableComponent getRankNameAndEloComponent(MatchType matchType, boolean ranked, int elo) {
      return matchType == MatchType.COMPETITIVE ? getRankNameAndEloComponent(ranked, elo) : Component.empty();
   }

   public static MutableComponent getRankNameAndEloAndPlaceComponent(boolean ranked, int elo, int place) {
      return ranked ? Component.empty().append(getRankNameComponent(elo)).append(" [").append(getEloComponent(elo)).append("] ").append(getPlaceComponent(place)) : UNRANKED_COMPONENT;
   }

   public static MutableComponent getEloAndChangeComponent(int elo, int eloChange) {
      return Component.empty().append(getEloComponent(elo)).append(Component.literal(String.format(" %s%d", eloChange >= 0 ? "+" : "", eloChange)).withStyle(eloChange == 0 ? ChatFormatting.YELLOW : (eloChange > 0 ? ChatFormatting.GREEN : ChatFormatting.RED)));
   }

   public static MutableComponent getRankAndEloAndChangeComponent(int elo, int eloChange) {
      return Component.empty().append(getRankNameComponent(elo)).append(" [").append(getEloComponent(elo)).append(Component.literal(String.format(" %s%d", eloChange >= 0 ? "+" : "", eloChange)).withStyle(eloChange == 0 ? ChatFormatting.YELLOW : (eloChange > 0 ? ChatFormatting.GREEN : ChatFormatting.RED))).append("]");
   }

   public static void renderHead(GuiGraphicsExtractor graphics, Identifier skin, int x, int y, int size) {
      if (skin != null) {
         graphics.blit(RenderPipelines.GUI_TEXTURED, skin, x, y, 8.0F, 8.0F, size, size, 8, 8, 8, 8, -1);
      }

   }

   static {
      UNRANKED_COMPONENT = Component.empty().append("Unranked").withStyle(ChatFormatting.GRAY);
   }
}
