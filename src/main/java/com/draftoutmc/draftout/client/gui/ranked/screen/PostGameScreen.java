package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutRunnable;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.RankedRank;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ChatComponent.DisplayMode;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class PostGameScreen extends Screen {
   private static final Component title = Component.empty();
   private final Map<LockoutMatchData.LockoutMatchPlayer, Integer> eloChanges;
   private static final int FF000000 = -16777216;
   private static final int margin = 36;
   private final EditBox oldChatBox;
   private EditBox chatBox;
   private final boolean cancelled;
   private final boolean draw;
   private final boolean won;
   private float eloChangeCurr = Float.NaN;
   private int eloFinal;
   private int eloChange;
   private long finalTimeMs;
   private boolean animationStarted = false;
   private final boolean hasEloChange;

   public PostGameScreen(Map<LockoutMatchData.LockoutMatchPlayer, Integer> eloChanges, EditBox oldChatBox, int magicFlags, long finalTimeMs) {
      super(title);
      this.eloChanges = eloChanges;
      this.oldChatBox = oldChatBox;
      this.cancelled = (magicFlags & 4) > 0;
      this.draw = (magicFlags & 2) > 0;
      this.won = (magicFlags & 1) > 0;
      this.finalTimeMs = finalTimeMs;
      Optional<LockoutMatchData.LockoutMatchPlayer> player = eloChanges.keySet().stream().filter((lmp) -> lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst();
      this.hasEloChange = player.isPresent() && ((LockoutMatchData.LockoutMatchPlayer)player.get()).ranked() && eloChanges.containsKey(player.get()) && eloChanges.get(player.get()) != null;
   }

   public void tick() {
      super.tick();
      Minecraft.getInstance().gui.tick();
      if (this.hasEloChange && !Float.isNaN(this.eloChangeCurr) && this.eloChangeCurr != 0.0F && this.animationStarted) {
         float friction = 0.87F;
         this.eloChangeCurr *= friction;
         if (Math.abs(this.eloChangeCurr) < 0.025F) {
            this.eloChangeCurr = 0.0F;
         }
      }

   }

   protected void init() {
      if (this.hasEloChange && Float.isNaN(this.eloChangeCurr)) {
         this.eloChanges.keySet().stream().filter((lmp) -> lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst().ifPresent((me) -> {
            this.eloChange = (Integer)this.eloChanges.get(me);
            this.eloFinal = me.elo() + this.eloChange;
            this.eloChangeCurr = (float)this.eloChange;
            LockoutRunnable animationTask = () -> this.animationStarted = true;
            animationTask.runTaskAt(System.currentTimeMillis() + 1500L);
         });
      }

      int buttonWidth = 100;
      int buttonHeight = 20;
      int margin = 8;
      int gap = 4;
      this.addRenderableWidget(Button.builder(Component.literal("Back to Main"), (bb) -> {
         LockoutMatchData.removeActiveMatch();
         ServerConnection.getInstance().sendMatchLeave();
         Minecraft.getInstance().disconnectFromWorld(Component.literal("Loading..."));
         Minecraft.getInstance().setScreen(new LockoutMainScreen());
      }).bounds(this.width - margin - buttonWidth, this.height - margin - buttonHeight, buttonWidth, buttonHeight).build());
      this.addRenderableWidget(Button.builder(Component.literal("Spectate world"), (bb) -> {
         LockoutMatchData.removeActiveMatch();
         ServerConnection.getInstance().sendMatchLeave();
         Minecraft.getInstance().setScreen((Screen)null);
      }).bounds(this.width - margin - gap - buttonWidth * 2, this.height - margin - buttonHeight, buttonWidth, buttonHeight).build());
      EditBox oldChatBox = this.chatBox != null ? this.chatBox : this.oldChatBox;
      int var10005 = this.height - buttonHeight - 8;
      this.chatBox = new EditBox(Minecraft.getInstance().font, 8, var10005, 160, buttonHeight, oldChatBox, Component.empty());
      this.chatBox.setMaxLength(256);
      this.chatBox.setVisible(true);
      this.setInitialFocus(this.chatBox);
      this.addRenderableWidget(this.chatBox);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() != 257 && event.key() != 335) {
         return this.chatBox.keyPressed(event) ? true : super.keyPressed(event);
      } else {
         this.sendChatMessage();
         return true;
      }
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      if (this.eloChanges.size() != 2) {
         super.extractRenderState(graphics, mouseX, mouseY, a);
      } else {
         int size = 24;
         Font font = Minecraft.getInstance().font;
         Lockout lockout = LockoutMatchData.getLockout();
         if (lockout == null) {
            super.extractRenderState(graphics, mouseX, mouseY, a);
         } else {
            Optional<LockoutMatchData.LockoutMatchPlayer> player = this.eloChanges.keySet().stream().filter((lmp) -> lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst();
            if (!player.isEmpty()) {
               LockoutBoard board = lockout.getBoard();
               LockoutMatchData.LockoutMatchPlayer me = (LockoutMatchData.LockoutMatchPlayer)player.get();
               LockoutMatchData.LockoutMatchPlayer opp = (LockoutMatchData.LockoutMatchPlayer)this.eloChanges.keySet().stream().filter((lmp) -> !lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst().get();
               int color = 2130706432 | me.lockoutTeamServer().getColor();
               graphics.fillGradient(0, 0, graphics.guiWidth(), graphics.guiHeight(), 0, color);
               int boardWidth = 14 + board.size() * 18;
               int boardHeight = 14 + board.size() * 18;
               int boardX = this.hasEloChange ? this.width / 2 + 50 : this.width / 2 - boardWidth / 2;
               int boardY = this.height / 2 - boardHeight / 2;
               Component pointsComponent = this.getScoreComponent(lockout);
               float pointsComponentScale = 1.25F;
               int var10002 = boardX + boardWidth / 2;
               float var10003 = (float)(boardY - 8);
               Objects.requireNonNull(font);
               RankedGuiUtils.scaledCenteredText(graphics, pointsComponent, var10002, (int)(var10003 - 9.0F * pointsComponentScale / 2.0F), pointsComponentScale, -16777216);
               Component text = this.cancelled ? Component.literal("CANCELLED!").withStyle(ChatFormatting.YELLOW) : (this.draw ? Component.literal("DRAW!").withStyle(ChatFormatting.YELLOW) : (this.won ? Component.literal("VICTORY!").withStyle(ChatFormatting.GREEN) : Component.literal("DEFEAT!").withStyle(ChatFormatting.RED)));
               RankedGuiUtils.scaledCenteredText(graphics, text, graphics.guiWidth() / 2, 12, 2.5F, -1);
               RankedGuiUtils.scaledCenteredText(graphics, Component.literal("Final time: " + Utility.msToTimer(this.finalTimeMs, 0)), boardX + boardWidth / 2, boardY + boardHeight + 4, 1.25F, -1);
               int y = this.height / 2 - (size + 24 + 12 + 22) / 2;
               int progressBarWidth = 120;
               int progressBarY = y + size + 16 + 4;
               int progressBarHeight = 8;
               int xCenter = this.width / 2 - 40 - boardWidth / 2;
               int x = xCenter - progressBarWidth / 2;
               if (this.hasEloChange) {
                  Identifier skin = me.skin();
                  Component usernameComponent = Component.literal(me.username());
                  float usernameScale = 2.0F;
                  int usernameSkinWidth = (int)((float)font.width(usernameComponent) * usernameScale + 8.0F + (float)size);
                  int xSkin = xCenter - usernameSkinWidth / 2;
                  if (skin != null) {
                     graphics.outline(xSkin - 1, y - 1, size + 2, size + 2, -1);
                     graphics.blit(RenderPipelines.GUI_TEXTURED, skin, xSkin, y, 8.0F, 8.0F, size, size, 8, 8, 8, 8, -1);
                  }

                  float animatedEloF = (float)this.eloFinal - this.eloChangeCurr;
                  int animatedElo = Math.round(animatedEloF);
                  int animatedChange = Math.round((float)this.eloChange - this.eloChangeCurr);
                  RankedRank currentRank = RankedRank.getFromElo(animatedElo);
                  RankedRank nextRank = currentRank.next();
                  Component eloChangeComponent = RankedGuiUtils.getEloAndChangeComponent(animatedElo, animatedChange);
                  RankedGuiUtils.scaledCenteredText(graphics, eloChangeComponent, xCenter, y + size + 8, 1.0F, -1);
                  float rankMin = (float)currentRank.getMinElo();
                  float rankMax = nextRank == null ? rankMin + 1.0F : (float)nextRank.getMinElo();
                  float progress = nextRank == null ? 1.0F : (animatedEloF - rankMin) / (rankMax - rankMin);
                  float ghostProgress = nextRank == null ? 1.0F : Math.clamp(((float)this.eloFinal - rankMin) / (rankMax - rankMin), 0.0F, 1.0F);
                  this.drawProgressBar(graphics, x, progressBarY, progressBarWidth, progressBarHeight, progress, ghostProgress, RankedRank.getFromElo(animatedElo).getColor());
                  Component leftRankComponent = Component.literal(currentRank.getDisplayName()).withColor(currentRank.getColor());
                  int var47 = x - font.width(leftRankComponent) - 4;
                  int var10004 = progressBarY + progressBarHeight / 2;
                  Objects.requireNonNull(font);
                  graphics.text(font, leftRankComponent, var47, var10004 - 9 / 2, currentRank.getColor());
                  Component leftRankMinEloComponent = Component.literal(String.valueOf(currentRank.getMinElo())).withColor(currentRank.getColor());
                  int var10000 = progressBarY + progressBarHeight / 2;
                  Objects.requireNonNull(font);
                  int rankMinEloHeight = var10000 - 9 / 2 + 8 + 2;
                  graphics.centeredText(font, leftRankMinEloComponent, x - font.width(leftRankComponent) / 2 - 4, rankMinEloHeight, currentRank.getColor());
                  if (nextRank != null) {
                     Component nextRankComponent = Component.literal(nextRank.getDisplayName()).withColor(nextRank.getColor());
                     var47 = x + progressBarWidth + 4;
                     var10004 = progressBarY + progressBarHeight / 2;
                     Objects.requireNonNull(font);
                     graphics.text(font, nextRankComponent, var47, var10004 - 9 / 2, nextRank.getColor());
                     Component nextRankMinEloComponent = Component.literal(String.valueOf(nextRank.getMinElo())).withColor(nextRank.getColor());
                     graphics.centeredText(font, nextRankMinEloComponent, x + progressBarWidth + 4 + font.width(nextRankComponent) / 2, rankMinEloHeight, nextRank.getColor());
                  }

                  var10002 = xSkin + size + 8;
                  float var49 = (float)(y + 2) + (float)size / 2.0F;
                  Objects.requireNonNull(font);
                  RankedGuiUtils.scaledText(graphics, usernameComponent, var10002, (int)(var49 - 9.0F * usernameScale / 2.0F), usernameScale, -1);
               }

               this.renderChatMessages(graphics, mouseX, mouseY);
               super.extractRenderState(graphics, mouseX, mouseY, a);
               this.drawBoard(graphics, mouseX, mouseY, boardX, boardY, board);
            }
         }
      }
   }

   private void drawProgressBar(GuiGraphicsExtractor graphics, int left, int top, int width, int height, float progress, float ghostProgress, int color) {
      boolean gain = ghostProgress > progress;
      int fillRight = left + Math.round(progress * (float)width);
      int mid = top + height / 2;
      int borderColor = ARGB.color(255, -6250336);
      graphics.outline(left - 1, top - 1, width + 2, height + 2, borderColor);
      graphics.fill(left, top, left + width, top + height, -16777216);
      int colorLight = this.lightenColor(color, 0.4F);
      graphics.fillGradient(left, top, fillRight, top + height, colorLight, color);
      int glossBottom = top + Math.max(1, height / 4);
      graphics.fillGradient(left, top, fillRight, glossBottom, 1157627903, 16777215);
      if (gain) {
         int ghostRight = left + Math.round(ghostProgress * (float)width);
         int ghostColor = this.lightenHalf(color);
         graphics.fillGradient(fillRight, top, ghostRight, mid, this.lightenColor(ghostColor, 0.2F), ghostColor);
         graphics.fillGradient(fillRight, mid, ghostRight, top + height, this.darkenHalf(ghostColor), ghostColor);
      } else {
         int ghostLeft = left + Math.round(ghostProgress * (float)width);
         graphics.fill(ghostLeft, top, fillRight, top + height, this.darkenHalf(color));
      }

   }

   private int lightenColor(int color, float amount) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      return -16777216 | Math.min(255, (int)((float)r + (float)(255 - r) * amount)) << 16 | Math.min(255, (int)((float)g + (float)(255 - g) * amount)) << 8 | Math.min(255, (int)((float)b + (float)(255 - b) * amount));
   }

   private int lightenHalf(int color) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      return -1610612736 | r / 2 + 128 << 16 | g / 2 + 128 << 8 | b / 2 + 128;
   }

   private int darkenHalf(int color) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      return -16777216 | r / 2 << 16 | g / 2 << 8 | b / 2;
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   private void renderChatMessages(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
      this.minecraft.gui.getChat().extractRenderState(graphics, this.font, Minecraft.getInstance().gui.getGuiTicks(), mouseX, mouseY, DisplayMode.BACKGROUND, false);
   }

   private void sendChatMessage() {
      String message = this.chatBox.getValue().trim();
      if (!message.isEmpty()) {
         if (!message.startsWith("/")) {
            if (Minecraft.getInstance().getConnection() != null) {
               Minecraft.getInstance().getConnection().sendChat(message);
               this.chatBox.setValue("");
            }
         }
      }
   }

   private Component getScoreComponent(Lockout lockout) {
      MutableComponent pointsComponent = Component.empty().append(Component.literal("Final score: ").withStyle(ChatFormatting.WHITE));

      for(LockoutTeam team : lockout.getTeams()) {
         pointsComponent.append(Component.literal(String.valueOf(team.getPoints(lockout))).withColor(team.getColor()));
         if (!team.equals(lockout.getTeams().getLast())) {
            pointsComponent.append(Component.literal("-").withStyle(ChatFormatting.GRAY));
         }
      }

      return pointsComponent;
   }

   private void drawBoard(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int boardX, int boardY, LockoutBoard board) {
      Utility.drawBingoBoardAt(graphics, Minecraft.getInstance().font, mouseX, mouseY, boardX, boardY, board);
      Goal hoveredGoal = Utility.getBoardHoveredGoal(graphics, mouseX, mouseY, board, boardX, boardY);
      if (hoveredGoal != null) {
         Utility.drawGoalInformation(graphics, this.font, hoveredGoal, mouseX, mouseY);
      }

   }
}
