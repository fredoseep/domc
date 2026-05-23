package com.draftoutmc.draftout.client.gui;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.LockoutClient;
import com.draftoutmc.draftout.client.gui.ranked.elements.CountdownButton;
import com.draftoutmc.draftout.client.gui.ranked.screen.ConfirmForfeitScreen;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class BoardScreen extends Screen {
   private Button drawButton;
   private Button forfeitButton;

   public BoardScreen() {
      super(Component.empty());
   }

   protected void init() {
      super.init();
      int buttonHeight = 20;
      this.addRenderableWidget(Button.builder(Component.literal("Close Board"), (bb) -> Minecraft.getInstance().setScreen((Screen)null)).width(100).pos(8, super.height - 8 - buttonHeight).build());
      LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
      if (matchData != null) {
         Lockout lockout = LockoutMatchData.getLockout();
         int boardWidth = Utility.getBoardWidth(lockout.getBoard().size());
         LockoutTeam playerTeam = lockout.getPlayerTeam(Minecraft.getInstance().getUser().getProfileId());
         boolean gameStarted = LockoutMatchData.getLockout().hasStarted();
         boolean showForfeitButton = !playerTeam.isForfeited() && gameStarted;
         boolean showDrawVoteButton = !playerTeam.isForfeited() && !matchData.requestedDrawVote() && gameStarted;
         if (showDrawVoteButton) {
            this.drawButton = CountdownButton.fromPlain((Button.Plain)Button.builder(Component.literal("Draw vote"), (bb) -> {
               Minecraft.getInstance().setScreen((Screen)null);
               matchData.requestedDrawVote(true);
               ServerConnection.getInstance().sendDrawVoteRequest();
            }).width(boardWidth).pos(super.width / 2 - boardWidth / 2, super.height / 2 + boardWidth / 2 + 4).build(), 1000L);
         } else {
            this.drawButton = Button.builder(Component.literal("Draw vote"), (b) -> {
            }).width(boardWidth).pos(super.width / 2 - boardWidth / 2, super.height / 2 + boardWidth / 2 + 4).build();
            this.drawButton.active = false;
         }

         if (showForfeitButton) {
            Button.OnPress handleButtonPress = (b) -> {
               if (LockoutMatchData.isInMatch() && (Boolean)ConfirmForfeitScreen.canForfeit.apply(lockout)) {
                  Minecraft.getInstance().setScreen(new ConfirmForfeitScreen());
               } else {
                  Minecraft.getInstance().setScreen((Screen)null);
               }
            };
            if ((Boolean)ConfirmForfeitScreen.canForfeit.apply(lockout)) {
               this.forfeitButton = CountdownButton.fromPlain((Button.Plain)Button.builder(Component.literal("Forfeit match"), handleButtonPress).width(boardWidth).pos(super.width / 2 - boardWidth / 2, super.height / 2 + boardWidth / 2 + buttonHeight + 8).build(), 1000L);
            } else {
               long canFfIn = lockout.getStartTimeMillis() + 60000L - System.currentTimeMillis();
               this.forfeitButton = CountdownButton.fromPlain((Button.Plain)Button.builder(Component.literal("Forfeit match"), handleButtonPress).width(boardWidth).pos(super.width / 2 - boardWidth / 2, super.height / 2 + boardWidth / 2 + buttonHeight + 8).build(), Math.max(1000L, canFfIn));
            }
         } else {
            this.forfeitButton = Button.builder(Component.literal("Forfeit match"), (b) -> {
            }).width(boardWidth).pos(super.width / 2 - boardWidth / 2, super.height / 2 + boardWidth / 2 + buttonHeight + 8).build();
            this.forfeitButton.active = false;
         }

         this.addRenderableWidget(this.drawButton);
         this.addRenderableWidget(this.forfeitButton);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (!Lockout.exists(lockout)) {
         this.onClose();
      } else {
         super.extractRenderState(context, mouseX, mouseY, delta);
         Font textRenderer = Minecraft.getInstance().font;
         LockoutBoard board = LockoutMatchData.getLockout().getBoard();
         Utility.drawCenterBingoBoard(context, textRenderer, mouseX, mouseY, board);
         Goal hoveredGoal = Utility.getBoardHoveredGoal(context, mouseX, mouseY, board);
         if (hoveredGoal != null) {
            Utility.drawGoalInformation(context, textRenderer, hoveredGoal, mouseX, mouseY);
         }

      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean keyPressed(KeyEvent event) {
      if (!LockoutClient.OPEN_BOARD_HOTKEY.matches(event) && !Minecraft.getInstance().options.keyInventory.matches(event)) {
         return super.keyPressed(event);
      } else {
         this.onClose();
         return true;
      }
   }
}
