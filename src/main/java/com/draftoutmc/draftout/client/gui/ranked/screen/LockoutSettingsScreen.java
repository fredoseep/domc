package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.LockoutClient;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.client.gui.ranked.elements.CycleSpeedSliderWidget;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.GoalDataGenerator;
import com.draftoutmc.draftout.lockout.GoalRegistry;
import com.draftoutmc.draftout.lockout.texture.CycleItemTexturesProvider;
import com.draftoutmc.draftout.lockout.texture.CycleTexturesProvider;
import com.draftoutmc.draftout.match.data.RankedData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.LinearLayout.Orientation;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import oshi.util.tuples.Pair;

public class LockoutSettingsScreen extends Screen {
   private static final Component TITLE = Component.literal("Settings");
   private Button connectTwitchButton;
   private long showBoardUntilMs;
   private static LockoutBoard DUMMY_BOARD = null;
   private static List<LockoutTeam> DUMMY_TEAMS = new ArrayList();

   public LockoutSettingsScreen() {
      super(TITLE);
   }

   private void showBoard() {
      this.showBoardUntilMs = System.currentTimeMillis() + 10000L;
   }

   protected void init() {
      int buttonWidth = 200;
      int buttonHeight = 20;
      int centerX = this.width / 2 - buttonWidth / 2;
      int startY = buttonHeight + 20;
      int gap = 4;
      LinearLayout linearLayout = (new LinearLayout(centerX, startY, Orientation.VERTICAL)).spacing(gap);
      linearLayout.addChild(Button.builder(this.getBoardPositionLabel(), (bb) -> {
         LockoutConfig.getInstance().boardPosition = LockoutConfig.getInstance().boardPosition == LockoutConfig.BoardPosition.LEFT ? LockoutConfig.BoardPosition.RIGHT : LockoutConfig.BoardPosition.LEFT;
         LockoutConfig.save();
         bb.setMessage(this.getBoardPositionLabel());
         this.showBoard();
      }).bounds(centerX, startY, buttonWidth, buttonHeight).build());
      this.connectTwitchButton = Button.builder(Component.literal("Connect Twitch"), (bb) -> {
         if (RankedData.linkedTwitchUsername() == null) {
            Minecraft.getInstance().setScreen(new LockoutWaitingScreen("Getting auth URL..."));
            ServerConnection.getInstance().sendTwitchAuthRequest();
         } else {
            Minecraft.getInstance().setScreen(new LockoutWaitingScreen("Disconnecting Twitch..."));
            ServerConnection.getInstance().sendTwitchDisconnectRequest();
         }

      }).bounds(centerX, startY + buttonHeight + gap, buttonWidth, buttonHeight).build();
      CycleSpeedSliderWidget cycleSpeedSlider = new CycleSpeedSliderWidget(centerX, startY + buttonHeight * 2 + gap * 2, buttonWidth, buttonHeight, LockoutConfig.getInstance().cycleSpeed, (value) -> {
         LockoutConfig.getInstance().cycleSpeed = value;
         LockoutConfig.save();
         LockoutClient.RENDER_TICK = 0;
         this.showBoard();
      });
      linearLayout.addChild(cycleSpeedSlider);
      if (RankedData.linkedTwitchUsername() != null) {
         this.connectTwitchButton.setTooltip(Tooltip.create(Component.literal("Connected as: ").append(Component.literal(RankedData.linkedTwitchUsername()).withColor(ARGB.color(255, 9520895)))));
      }

      linearLayout.addChild(this.connectTwitchButton);
      this.addRenderableWidget(Button.builder(Component.literal("Back"), (bb) -> Minecraft.getInstance().setScreen(new LockoutMainScreen())).bounds(8, this.height - 8 - buttonHeight, 80, buttonHeight).build());
      linearLayout.arrangeElements();
      linearLayout.visitWidgets(this::addRenderableWidget);
   }

   private Component getBoardPositionLabel() {
      String side = LockoutConfig.getInstance().boardPosition == LockoutConfig.BoardPosition.LEFT ? "Left" : "Right";
      return Component.literal("Board Position: " + side);
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
      super.extractRenderState(graphics, mouseX, mouseY, delta);
      graphics.centeredText(this.font, TITLE, this.width / 2, 20, -1);
      if (this.showBoardUntilMs > System.currentTimeMillis()) {
         if (DUMMY_BOARD == null) {
            this.buildDummyBoard(graphics);
         }

         Utility.drawBoardOverlay(graphics, DUMMY_BOARD, DUMMY_TEAMS, "00:00");
      }

   }

   private void buildDummyBoard(GuiGraphicsExtractor graphics) {
      List<Pair<String, String>> goals = new ArrayList();
      Set<String> selected = new HashSet();
      List<String> goalPool = GoalRegistry.INSTANCE.getRegisteredGoals();
      boolean hasCycleTexturesGoal = false;

      while(true) {
         String id;
         String data;
         while(true) {
            if (goals.size() >= 25) {
               if (!hasCycleTexturesGoal) {
                  this.buildDummyBoard(graphics);
                  return;
               }

               DUMMY_BOARD = new LockoutBoard(goals);
               return;
            }

            id = (String)goalPool.get((int)(Math.random() * (double)goalPool.size()));
            if (!selected.contains(id)) {
               data = "null";
               if (GoalRegistry.INSTANCE.getDataGenerator(id).isPresent()) {
                  data = ((GoalDataGenerator)GoalRegistry.INSTANCE.getDataGenerator(id).get()).generateData(GoalDataGenerator.ALL_DYES);
               }

               try {
                  Goal goal = GoalRegistry.INSTANCE.newGoal(id, data);
                  if (goal instanceof CycleTexturesProvider || goal instanceof CycleItemTexturesProvider) {
                     hasCycleTexturesGoal = true;
                  }
                  break;
               } catch (Exception var9) {
               }
            }
         }

         selected.add(id);
         goals.add(new Pair(id, data));
      }
   }

   public void tick() {
      super.tick();
      this.connectTwitchButton.setMessage(Component.literal(RankedData.linkedTwitchUsername() == null ? "Connect Twitch" : "Disconnect Twitch"));
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   static {
      for(int i = 0; i < 2; ++i) {
         DUMMY_TEAMS.add(new LockoutTeam(List.of("Player" + (i + 1)), i == 0 ? ChatFormatting.RED.getColor() : ChatFormatting.AQUA.getColor()));
      }

   }
}
