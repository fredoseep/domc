package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.data.MatchHistoryData;
import com.draftoutmc.draftout.match.data.MatchOutcome;
import com.draftoutmc.draftout.match.data.PlayerProfile;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class MatchDataWidget extends AbstractWidget {
   private MatchHistoryData.MatchResult matchResult;
   private static final int PLAYER_HEAD_SIZE = 30;
   private List<Goal> completedGoals;
   private Map<Goal, Integer> indexMap = new HashMap();
   private final PlayerProfile viewingProfile;
   private PlayerProfile opponent;
   private Component pointsComponent;

   public MatchDataWidget(PlayerProfile viewingProfile, int x, int y, int width, int height) {
      super(x, y, width, height, Component.empty());
      this.viewingProfile = viewingProfile;
   }

   public void setMatchResult(MatchHistoryData.MatchResult matchResult, Map<String, PlayerProfile> profiles) {
      this.matchResult = matchResult;
      this.opponent = this.findOpponent(profiles);
      this.completedGoals = matchResult.board().getGoals().stream().filter(Goal::isCompleted).sorted(Comparator.comparing(Goal::getCompletedAtMs).reversed()).toList();
      this.pointsComponent = Component.empty().append(Component.literal(String.valueOf(((MatchHistoryData.PlayerResult)matchResult.players().get(this.viewingProfile.uuid().toString())).score())).withColor(((LockoutTeam)matchResult.lockoutTeamMap().get(this.viewingProfile.uuid().toString())).getColor())).append(Component.literal(" - ").withStyle(ChatFormatting.GRAY)).append(Component.literal(String.valueOf(((MatchHistoryData.PlayerResult)matchResult.players().get(this.opponent.uuid().toString())).score())).withColor(((LockoutTeam)matchResult.lockoutTeamMap().get(this.opponent.uuid().toString())).getColor()));
      this.indexMap.clear();
      int idx = 0;

      for(Goal goal : matchResult.board().getGoals()) {
         this.indexMap.put(goal, idx++);
      }

   }

   private PlayerProfile findOpponent(Map<String, PlayerProfile> profiles) {
      for(MatchHistoryData.PlayerResult p : this.matchResult.players().values()) {
         if (!p.uuid().equals(this.viewingProfile.uuid().toString())) {
            return (PlayerProfile)profiles.get(p.uuid());
         }
      }

      return null;
   }

   protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      Font font = Minecraft.getInstance().font;
      if (this.matchResult == null) {
         this.renderPlayerInfo(graphics, this.viewingProfile.username(), this.viewingProfile.skin(), this.viewingProfile.elo(), this.viewingProfile.ranked(), this.viewingProfile.rank(), true, false, -1, false, false);
         MutableComponent var15 = Component.literal("Click on a match to see details.").withStyle(ChatFormatting.GRAY);
         int var10003 = this.getX() + this.getWidth() / 2;
         int var10004 = this.getY() + this.getHeight() / 2;
         Objects.requireNonNull(font);
         graphics.centeredText(font, var15, var10003, var10004 - 9 / 2, -1);
      } else {
         LockoutBoard board = this.matchResult.board();
         int boardWidth = 14 + board.size() * 18;
         int boardHeight = 14 + board.size() * 18;
         int boardX = this.getX() + this.getWidth() / 2 - boardWidth / 2;
         int boardY = 90;
         Utility.drawBingoBoardAt(graphics, font, mouseX, mouseY, boardX, boardY, board);
         Goal hoveredGoal = Utility.getBoardHoveredGoal(graphics, mouseX, mouseY, board, boardX, boardY);
         if (hoveredGoal != null) {
            Utility.drawGoalInformation(graphics, font, hoveredGoal, mouseX, mouseY);
         }

         MutableComponent var10001 = Component.literal(this.matchResult.outcome().getDisplay());
         int var10002 = this.getX() + this.getWidth() / 2;
         Objects.requireNonNull(font);
         RankedGuiUtils.scaledCenteredText(graphics, var10001, var10002, boardY - (int)(9.0F * 1.5F) * 2 - 4, 1.5F, -1);
         Component var12 = this.pointsComponent;
         var10002 = this.getX() + this.getWidth() / 2;
         Objects.requireNonNull(font);
         RankedGuiUtils.scaledCenteredText(graphics, var12, var10002, boardY - (int)(9.0F * 1.5F) - 2, 1.5F, -1);
         this.renderPlayerInfo(graphics, this.viewingProfile.username(), this.viewingProfile.skin(), this.viewingProfile.elo(), this.viewingProfile.ranked(), this.viewingProfile.rank(), true, true, Integer.MIN_VALUE | ((LockoutTeam)this.matchResult.lockoutTeamMap().get(this.viewingProfile.uuid().toString())).getColor(), !MatchOutcome.isDraw(this.matchResult.outcome()), ((MatchHistoryData.PlayerResult)this.matchResult.players().get(this.viewingProfile.uuid().toString())).won());
         this.renderPlayerInfo(graphics, this.opponent.username(), this.opponent.skin(), this.opponent.elo(), this.opponent.ranked(), this.opponent.rank(), false, true, Integer.MIN_VALUE | ((LockoutTeam)this.matchResult.lockoutTeamMap().get(this.opponent.uuid().toString())).getColor(), !MatchOutcome.isDraw(this.matchResult.outcome()), ((MatchHistoryData.PlayerResult)this.matchResult.players().get(this.opponent.uuid().toString())).won());
         long var13 = (long)this.matchResult.durationMs();
         RankedGuiUtils.scaledCenteredText(graphics, Component.literal("Final time: " + Utility.msToTimer(var13, 0)), this.getX() + this.getWidth() / 2, boardY + boardHeight + 4, 1.25F, -1);
      }
   }

   private void renderPlayerInfo(GuiGraphicsExtractor graphics, String username, Identifier skin, int elo, boolean ranked, int rank, boolean left, boolean drawGradient, int gradientColor, boolean renderWon, boolean won) {
      Font font = Minecraft.getInstance().font;
      int edgeGap = 4;
      int headX = left ? this.getX() + 4 : this.getX() + this.getWidth() - 4 - 30;
      int headY = 9;
      if (drawGradient) {
         int otherColor = 285212671 & gradientColor;
         int centerX = this.getX() + this.getWidth() / 2;
         int cornerX = left ? headX - 2 : headX + 30 + 2;
         RankedGuiUtils.rightToLeftGradient(graphics, Math.min(cornerX, centerX), headY - 2, Math.max(cornerX, centerX), headY + 30 + 2, left ? otherColor : gradientColor, left ? gradientColor : otherColor);
      }

      RankedGuiUtils.renderHead(graphics, skin, headX, headY, 30);
      int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
      Component nameComponent = Component.literal(username);
      float nameScale = guiScale >= 4 ? 1.5F : 1.0F;
      int nameX = left ? headX + 30 + 4 : headX - 4 - (int)((float)font.width(nameComponent) * nameScale);
      RankedGuiUtils.scaledText(graphics, nameComponent, nameX, headY, nameScale, -1);
      Component rankComponent = RankedGuiUtils.getRankNameAndEloAndPlaceComponent(ranked, elo, rank);
      float rankScale = guiScale >= 2 ? 0.75F : 1.0F;
      int rankX = left ? headX + 30 + 4 : headX - 4 - (int)((float)font.width(rankComponent) * rankScale);
      Objects.requireNonNull(font);
      int rankY = headY + (int)(9.0F * nameScale) + 2;
      RankedGuiUtils.scaledText(graphics, rankComponent, rankX, rankY, rankScale, -1);
      if (renderWon) {
         Component wonComponent = Component.literal(won ? "WON!" : "LOST!").withStyle(won ? ChatFormatting.GREEN : ChatFormatting.RED);
         RankedGuiUtils.outlinedText(graphics, wonComponent, headX + 15 - (int)((float)font.width(wonComponent) * 1.25F / 2.0F), headY + 30 + 4, 1.25F, -1, -16777216);
      }

   }

   protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
   }

   public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
      return true;
   }
}
