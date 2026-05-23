package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.data.MatchHistoryData;
import com.draftoutmc.draftout.match.data.MatchOutcome;
import com.draftoutmc.draftout.match.data.PlayerProfile;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class MatchListEntry extends AbstractSelectionList.Entry<MatchListEntry> {
   private static final int PADDING_RIGHT = 10;
   private static final int LINE_SPACING = 2;
   private static final int HEAD_SIZE = 20;
   private static final int INNER_PAD = 4;
   private final int BASE_GREEN = 1347813205;
   private final int BASE_RED = 1358910805;
   private final int BASE_BLUE = 1347769855;
   private final MatchHistoryData.MatchResult match;
   private final String lookingAtPlayerUuid;
   private final Map<String, PlayerProfile> profiles;
   private final Consumer<MatchListEntry> onClick;

   public MatchListEntry(MatchHistoryData.MatchResult match, String lookingAtPlayerUuid, Map<String, PlayerProfile> profiles, Consumer<MatchListEntry> onClick) {
      this.match = match;
      this.lookingAtPlayerUuid = lookingAtPlayerUuid;
      this.profiles = profiles;
      this.onClick = onClick;
   }

   public int getWidth() {
      return super.getWidth() - 10;
   }

   public void extractContent(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float alpha) {
      Font font = Minecraft.getInstance().font;
      int bgRgb;
      if (MatchOutcome.isDraw(this.match.outcome())) {
         bgRgb = 1347769855;
      } else if (this.findSelf().won()) {
         bgRgb = 1347813205;
      } else {
         bgRgb = 1358910805;
      }

      RankedGuiUtils.rightToLeftGradient(graphics, this.getX() - 12, this.getY(), this.getX(), this.getY() + this.getHeight(), bgRgb, bgRgb & 16777215);
      graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bgRgb);
      RankedGuiUtils.rightToLeftGradient(graphics, this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth() + 12, this.getY() + this.getHeight(), bgRgb & 16777215, bgRgb);
      if (hovered) {
         int overlay = 419430399;
         RankedGuiUtils.rightToLeftGradient(graphics, this.getX() - 12, this.getY(), this.getX(), this.getY() + this.getHeight(), overlay, overlay & 16777215);
         graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), overlay);
         RankedGuiUtils.rightToLeftGradient(graphics, this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth() + 12, this.getY() + this.getHeight(), overlay & 16777215, overlay);
      }

      int cx = this.getContentX();
      int midY = this.getY() + this.getHeight() / 2;
      MatchHistoryData.PlayerResult opponent = this.findOpponent();
      Identifier opponentSkin = opponent != null ? ((PlayerProfile)this.profiles.get(opponent.uuid())).skin() : null;
      RankedGuiUtils.renderHead(graphics, opponentSkin, cx, midY - 10, 20);
      int textX = cx + 20 + 8;
      Objects.requireNonNull(font);
      int lineHeight = 9 + 2;
      int var10000 = midY - 1;
      Objects.requireNonNull(font);
      int topY = var10000 - 9;
      String opponentName = opponent.name();
      Component separator = Component.literal(" · ").withStyle(ChatFormatting.GRAY);
      Component opponentComponent = Component.empty().append(Component.literal(opponentName).withStyle(ChatFormatting.WHITE)).append(separator);
      String scoreStr = this.buildScore();
      graphics.text(font, opponentComponent, textX, topY, -1);
      graphics.text(font, Component.literal(scoreStr).withStyle(ChatFormatting.WHITE), textX + font.width(opponentComponent), topY, -1);
      String modeName = MatchType.match(this.match.matchType()).getDisplayName();
      Component modeComponent = Component.literal(modeName).withStyle(ChatFormatting.GRAY).append(separator);
      String timePart = timeSince(this.match.completedAt());
      graphics.text(font, modeComponent, textX, topY + lineHeight, -1);
      graphics.text(font, Component.literal(timePart).withStyle(ChatFormatting.GRAY), textX + font.width(modeComponent), topY + lineHeight, -1);
   }

   private MatchHistoryData.PlayerResult findOpponent() {
      for(String uuid : this.match.players().keySet()) {
         if (!uuid.equals(this.lookingAtPlayerUuid)) {
            return (MatchHistoryData.PlayerResult)this.match.players().get(uuid);
         }
      }

      return null;
   }

   private MatchHistoryData.PlayerResult findSelf() {
      return (MatchHistoryData.PlayerResult)this.match.players().get(this.lookingAtPlayerUuid);
   }

   private String buildScore() {
      MatchHistoryData.PlayerResult self = this.findSelf();
      MatchHistoryData.PlayerResult opponent = this.findOpponent();
      if (self != null && opponent != null && !self.uuid().equals(opponent.uuid())) {
         int var10000 = self.score();
         return var10000 + " - " + opponent.score();
      } else {
         return "-";
      }
   }

   private static String timeSince(long completedAtMs) {
      long diff = System.currentTimeMillis() - completedAtMs;
      long mins = diff / 60000L;
      if (mins < 1L) {
         return "Just now";
      } else if (mins < 60L) {
         return mins + "m ago";
      } else {
         long hours = mins / 60L;
         if (hours < 24L) {
            return hours + "h ago";
         } else {
            long days = hours / 24L;
            return days < 30L ? hours / 24L + "d ago" : days / 30L + "mo ago";
         }
      }
   }

   public static int computeHeight() {
      return 28;
   }

   public boolean isMouseOver(double mouseX, double mouseY) {
      return mouseX >= (double)this.getX() && mouseX <= (double)(this.getX() + this.getWidth()) && mouseY >= (double)this.getY() && mouseY <= (double)(this.getY() + this.getHeight());
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      if (event.button() == 0) {
         this.onClick.accept(this);
         return true;
      } else {
         return false;
      }
   }

   @Generated
   public MatchHistoryData.MatchResult getMatch() {
      return this.match;
   }
}
