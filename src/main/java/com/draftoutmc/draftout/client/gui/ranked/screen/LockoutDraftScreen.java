package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ChatComponent.DisplayMode;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import oshi.util.tuples.Pair;

public class LockoutDraftScreen extends Screen {
   private static final Component title = Component.empty();
   private final List<LockoutMatchData.LockoutMatchPlayer> players;
   private final List<Goal> goalPool;
   private Button gameMenuButton;
   private Button viewWorldButton;
   private final EditBox oldChatBox;
   private EditBox chatBox;
   private boolean canExitDraftScreen = false;
   private static final int LEFT_MOUSE_BUTTON = 0;
   private static float minSaturation = 0.25F;

   public LockoutDraftScreen(List<LockoutMatchData.LockoutMatchPlayer> players, List<Pair<String, String>> goalPool, EditBox oldChatBox) {
      super(title);
      this.players = players;
      this.oldChatBox = oldChatBox;
      this.goalPool = LockoutBoard.toGoals(goalPool);
   }

   protected void init() {
      int buttonWidth = 80;
      int buttonHeight = 20;
      this.gameMenuButton = Button.builder(Component.literal("Game menu"), (bb) -> {
         if (this.canExitDraftScreen) {
            Minecraft.getInstance().setScreen(new PauseScreen(true));
         }

      }).bounds(this.width - 8 - buttonWidth, this.height - 8 - buttonHeight, buttonWidth, buttonHeight).build();
      this.viewWorldButton = Button.builder(Component.literal("View world"), (bb) -> {
         if (this.canExitDraftScreen) {
            Minecraft.getInstance().setScreen((Screen)null);
         }

      }).bounds(this.width - 16 - buttonWidth * 2, this.height - 8 - buttonHeight, buttonWidth, buttonHeight).build();
      this.addRenderableWidget(this.gameMenuButton);
      this.addRenderableWidget(this.viewWorldButton);
      EditBox oldChatBox = this.chatBox != null ? this.chatBox : this.oldChatBox;
      int var10005 = this.height - buttonHeight - 8;
      this.chatBox = new EditBox(Minecraft.getInstance().font, 8, var10005, 160, buttonHeight, oldChatBox, Component.empty());
      this.chatBox.setMaxLength(256);
      this.chatBox.setVisible(true);
      this.setInitialFocus(this.chatBox);
      this.addRenderableWidget(this.chatBox);
   }

   public void tick() {
      super.tick();
      this.canExitDraftScreen = !LockoutMatchData.isInMatch() || LockoutMatchData.CURRENT_MATCH.picksNext() != null;
      this.gameMenuButton.active = this.canExitDraftScreen;
      this.viewWorldButton.active = this.canExitDraftScreen;
      Minecraft.getInstance().gui.tick();
   }

   public boolean shouldCloseOnEsc() {
      return this.canExitDraftScreen;
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      if (LockoutMatchData.isInMatch()) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         LockoutBoard board = matchData.pickedBoard();
         boolean imPicking = matchData.picksNext() != null && matchData.picksNext().equals(Minecraft.getInstance().getUser().getProfileId());
         int width = graphics.guiWidth();
         int height = graphics.guiHeight();
         Font font = Minecraft.getInstance().font;
         int boardWidth = 14 + board.size() * 18;
         int x = width / 2 - boardWidth / 2;
         int boardHeight = 14 + board.size() * 18;
         int y = height / 2 - 16 - (matchData.draftIdx() >= 0 && matchData.draftIdx() < 25 ? 0 : 18);
         RankedGuiUtils.scaledCenteredText(graphics, Component.literal("DRAFTOUT"), width / 2, 12, 2.5F, -1);
         Utility.drawBingoBoardAt(graphics, font, mouseX, mouseY, x, y, board);
         Goal hoveredGoal = Utility.getBoardHoveredGoal(graphics, mouseX, mouseY, board, x, y);
         int margin = 8;
         int borderColor = ARGB.color(255, -6250336);
         float pickProgress = Math.max(0.0F, (float)((double)(matchData.pickEndsAt() - System.currentTimeMillis()) / (double)matchData.pickTimeMs()));
         if (matchData.draftIdx() >= 0 && matchData.draftIdx() < 25) {
            LockoutMatchData.LockoutMatchPlayer lmp = (LockoutMatchData.LockoutMatchPlayer)this.players.stream().filter((p) -> p.uuid().equals(matchData.picksNext())).findFirst().get();
            int color = lmp.lockoutTeamServer().getColor();
            color = Utility.brightness(color, 0.5F);
            String playerName = lmp.username();
            String text = imPicking ? "You are picking a goal." : playerName + " is picking a goal.";
            int textWidth = font.width(text);
            int textX = width / 2;
            int textY = height / 2 - 14 - 20 - 2 - 14 - 24;
            int var50 = textX - textWidth / 2 - margin;
            Objects.requireNonNull(font);
            int var54 = textY - 9 / 2 - margin;
            int var60 = textX + textWidth / 2 + margin;
            Objects.requireNonNull(font);
            graphics.fill(var50, var54, var60, textY + 9 / 2 + margin, color);
            var50 = textX - textWidth / 2 - margin;
            Objects.requireNonNull(font);
            var54 = textY - 9 / 2 - margin;
            var60 = textWidth + margin * 2;
            Objects.requireNonNull(font);
            graphics.outline(var50, var54, var60, 9 + margin * 2, borderColor);
            MutableComponent var56 = Component.literal(text);
            var60 = width / 2;
            Objects.requireNonNull(font);
            graphics.centeredText(font, var56, var60, textY - 9 / 2, -1);
            int var57 = textX - textWidth / 2 - margin;
            Objects.requireNonNull(font);
            this.drawProgressBar(graphics, var57, textY + 9 / 2 + margin + 1, textWidth + margin * 2, 1, pickProgress);
            int startIdx = matchData.draftIdx() * 2;
            Goal leftGoal = (Goal)this.goalPool.get(startIdx);
            Goal rightGoal = (Goal)this.goalPool.get(startIdx + 1);
            Utility.drawDraftPicks(graphics, Minecraft.getInstance().font, mouseX, mouseY, board, leftGoal, rightGoal, matchData.picked());
         } else {
            int color = ARGB.alpha(255);
            String text = "";
            if (matchData.draftIdx() <= 0) {
               text = "Draft is starting...";
            }

            if (matchData.draftIdx() >= 25) {
               text = "Board drafted! Starting lockout...";
            }

            int textWidth = font.width(text);
            int textX = width / 2;
            int textY = height / 2 - 14 - 20 - 2 - 19;
            int var10001 = textX - textWidth / 2 - margin;
            Objects.requireNonNull(font);
            int var10002 = textY - 9 / 2 - margin;
            int var10003 = textX + textWidth / 2 + margin;
            Objects.requireNonNull(font);
            graphics.fill(var10001, var10002, var10003, textY + 9 / 2 + margin, color);
            var10001 = textX - textWidth / 2 - margin;
            Objects.requireNonNull(font);
            var10002 = textY - 9 / 2 - margin;
            var10003 = textWidth + margin * 2;
            Objects.requireNonNull(font);
            graphics.outline(var10001, var10002, var10003, 9 + margin * 2, borderColor);
            MutableComponent var53 = Component.literal(text);
            var10003 = width / 2;
            Objects.requireNonNull(font);
            graphics.centeredText(font, var53, var10003, textY - 9 / 2, -1);
         }

         float myColorSaturation = matchData.picksNext() == null ? 1.0F : getSaturation(imPicking, pickProgress, matchData.pickTimeMs(), true);
         float oppColorSaturation = matchData.picksNext() == null ? 1.0F : getSaturation(imPicking, pickProgress, matchData.pickTimeMs(), false);

         for(LockoutMatchData.LockoutMatchPlayer lmp : this.players) {
            Identifier skin = lmp.skin();
            boolean isMe = lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId());
            y = 8;
            int size = 48;
            String username = lmp.username();
            int color = 2130706432 | lmp.lockoutTeamServer().getColor();
            if (isMe) {
               x = 8;
               RankedGuiUtils.rightToLeftGradient(graphics, 0, 0, graphics.guiWidth() / 2, graphics.guiHeight(), 0, muteColor(color, myColorSaturation));
               if (skin != null) {
                  graphics.outline(x - 1, y - 1, size + 2, size + 2, -1);
                  graphics.blit(RenderPipelines.GUI_TEXTURED, skin, x, y, 8.0F, 8.0F, size, size, 8, 8, 8, 8, -1);
               }

               RankedGuiUtils.scaledText(graphics, Component.literal(username), x, y + size + 8, 2.0F, -1);
               RankedGuiUtils.scaledText(graphics, RankedGuiUtils.getRankNameAndEloComponent(matchData.matchType(), lmp.ranked(), lmp.elo()), x, y + size + 8 + 16 + 4, 1.0F, -1);
            } else {
               x = width - 8;
               int skinLeftX = x - size;
               RankedGuiUtils.rightToLeftGradient(graphics, graphics.guiWidth() / 2, 0, graphics.guiWidth(), graphics.guiHeight(), muteColor(color, oppColorSaturation), 0);
               if (skin != null) {
                  graphics.outline(skinLeftX - 1, y - 1, size + 2, size + 2, -1);
                  graphics.blit(RenderPipelines.GUI_TEXTURED, skin, skinLeftX, y, 8.0F, 8.0F, size, size, 8, 8, 8, 8, -1);
               }

               Component eloRank = RankedGuiUtils.getEloAndRankNameComponent(matchData.matchType(), lmp.ranked(), lmp.elo());
               RankedGuiUtils.scaledText(graphics, Component.literal(username), x - font.width(username) * 2, y + size + 8, 2.0F, -1);
               RankedGuiUtils.scaledText(graphics, eloRank, x - font.width(eloRank), y + size + 8 + 16 + 4, 1.0F, -1);
            }
         }

         this.renderChatMessages(graphics, mouseX, mouseY);
         super.extractRenderState(graphics, mouseX, mouseY, a);
         if (hoveredGoal != null) {
            Utility.drawGoalInformation(graphics, font, hoveredGoal, mouseX, mouseY);
         }

      }
   }

   private void drawProgressBar(GuiGraphicsExtractor graphics, int left, int top, int width, int height, float progress) {
      graphics.fill(left, top, left + width, top + height, -16777216);
      graphics.fill(left, top, left + Math.round(progress * (float)width), top + height, -16711936);
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   public boolean keyPressed(KeyEvent event) {
      if (event.key() != 257 && event.key() != 335) {
         return this.chatBox.keyPressed(event) ? true : super.keyPressed(event);
      } else {
         this.sendChatMessage();
         return true;
      }
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      int button = event.button();
      Optional<Integer> hoveredIdx = Utility.getDraftHoveredIdx(this.width, this.height, (int)event.x(), (int)event.y());
      LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
      if (LockoutMatchData.isInMatch() && matchData.picksNext() != null) {
         boolean imPicking = matchData.picksNext().equals(Minecraft.getInstance().getUser().getProfileId());
         if (LockoutMatchData.isInMatch() && imPicking && !matchData.picked()) {
            if (button == 0 && hoveredIdx.isPresent()) {
               ServerConnection.getInstance().sendDraftSelection((Integer)hoveredIdx.get() == 0, matchData.draftIdx());
               matchData.picked(true);
               return true;
            } else {
               return super.mouseClicked(event, doubleClick);
            }
         } else {
            return super.mouseClicked(event, doubleClick);
         }
      } else {
         return super.mouseClicked(event, doubleClick);
      }
   }

   private static int muteColor(int color, float saturation) {
      int a = ARGB.alpha(color);
      int r = ARGB.red(color);
      int g = ARGB.green(color);
      int b = ARGB.blue(color);
      int gray = (int)((double)r * 0.299 + (double)g * 0.587 + (double)b * 0.114);
      return ARGB.color(a, (int)((float)gray + saturation * (float)(r - gray)), (int)((float)gray + saturation * (float)(g - gray)), (int)((float)gray + saturation * (float)(b - gray)));
   }

   private static float getSaturation(boolean imPicking, float pickProgress, long pickTimeMs, boolean forMe) {
      float elapsed = (float)pickTimeMs * (1.0F - pickProgress);
      float t = Math.min(1.0F, elapsed / 500.0F);
      t = t * t * (3.0F - 2.0F * t);
      float targetMe = imPicking ? 1.0F : minSaturation;
      float prevMe = imPicking ? minSaturation : 1.0F;
      float targetOpp = imPicking ? minSaturation : 1.0F;
      float prevOpp = imPicking ? 1.0F : minSaturation;
      float ourSat = prevMe + t * (targetMe - prevMe);
      float oppSat = prevOpp + t * (targetOpp - prevOpp);
      return forMe ? ourSat : oppSat;
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
}
