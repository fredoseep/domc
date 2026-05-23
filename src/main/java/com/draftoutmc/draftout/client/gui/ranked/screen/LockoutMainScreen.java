package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.client.gui.ranked.elements.MainScreenButton;
import com.draftoutmc.draftout.client.gui.ranked.elements.PlayButton;
import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.PlayerProfile;
import com.draftoutmc.draftout.match.data.RankedData;
import com.draftoutmc.draftout.match.data.RankedRank;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class LockoutMainScreen extends Screen {
   private static final Component title = Component.empty();
   private Button queueCompetitiveButton;
   private Button queueQuickPlayButton;
   private Button leaderboardButton;
   private Button settingsButton;
   private Button searchProfileButton;
   private final Button.OnPress onDraftoutQueueButtonPress = (bb) -> {
      if (!ServerConnection.getInstance().queued()) {
         ServerConnection sc = ServerConnection.getInstance();
         sc.joinQueue(MatchType.COMPETITIVE);
         this.rebuildWidgets();
      } else {
         bb.active = false;
         ServerConnection sc = ServerConnection.getInstance();
         sc.leaveQueue();
         this.rebuildWidgets();
      }

   };
   private final Button.OnPress onQuickPlayQueueButtonPress = (bb) -> {
      if (!ServerConnection.getInstance().queued()) {
         ServerConnection sc = ServerConnection.getInstance();
         sc.joinQueue(MatchType.QUICK_PLAY);
         this.rebuildWidgets();
      } else {
         bb.active = false;
         ServerConnection sc = ServerConnection.getInstance();
         sc.leaveQueue();
         this.rebuildWidgets();
      }

   };
   private static final int logoSize = 32;
   private static final int logoTitleGap = 4;
   private static final float titleScale = 3.0F;
   private static final Component titleComponent = Component.literal("DRAFTOUT");

   public LockoutMainScreen() {
      super(title);
   }

   protected void init() {
      int queueButtonWidth = 120;
      int queueButtonHeight = 32;
      int otherButtonWidth = 120;
      int otherButtonHeight = 32;
      int edgeGap = 8;
      int buttonGap = 2;
      this.queueCompetitiveButton = new PlayButton(this.width - 120 - 8, this.height - 8 - 32, 120, 32, "Competitive", this.onDraftoutQueueButtonPress, Identifier.fromNamespaceAndPath("draftout", "recovery_compass_16"));
      this.queueCompetitiveButton.active = !LockoutMatchData.isInMatch();
      this.addRenderableWidget(this.queueCompetitiveButton);
      this.queueQuickPlayButton = new PlayButton(this.width - 120 - 8, this.height - 8 - 2 - 64, 120, 32, "Quick Play", this.onQuickPlayQueueButtonPress, Identifier.fromNamespaceAndPath("draftout", "compass_16"));
      this.queueQuickPlayButton.active = !LockoutMatchData.isInMatch();
      this.addRenderableWidget(this.queueQuickPlayButton);
      Identifier skin = PlayerProfile.SKIN_RENDERER.getMySkin();
      Button profileButton = new MainScreenButton(8, this.height - 8 - 160 - 8, 120, 32, "Profile", (bb) -> {
         Minecraft mc = Minecraft.getInstance();
         ServerConnection.getInstance().setPreviousScreen();
         ServerConnection.getInstance().sendProfileRequest(RankedData.myProfile().uuid());
         mc.setScreen(new LockoutWaitingScreen("Loading profile..."));
      }, skin);
      this.addRenderableWidget(profileButton);
      this.settingsButton = new MainScreenButton(8, this.height - 8 - 64 - 2, 120, 32, "Settings", (bb) -> Minecraft.getInstance().setScreen(new LockoutSettingsScreen()), Identifier.withDefaultNamespace("textures/item/iron_nautilus_armor.png"));
      this.addRenderableWidget(this.settingsButton);
      this.leaderboardButton = new MainScreenButton(8, this.height - 8 - 96 - 4, 120, 32, "Leaderboard", (bb) -> {
         Minecraft.getInstance().setScreen(new LockoutWaitingScreen("Fetching leaderboard..."));
         ServerConnection.getInstance().sendLeaderboardRequest();
      }, Identifier.withDefaultNamespace("textures/item/golden_spear.png"));
      this.addRenderableWidget(this.leaderboardButton);
      this.searchProfileButton = new MainScreenButton(8, this.height - 8 - 128 - 6, 120, 32, "Search Profile", (bb) -> {
         Minecraft mc = Minecraft.getInstance();
         mc.setScreen(new FindProfileScreen());
      }, Identifier.withDefaultNamespace("textures/item/wind_charge.png"));
      this.addRenderableWidget(this.searchProfileButton);
      Button backToMainButton = new MainScreenButton(8, this.height - 8 - 32, 120, 32, "Back to Title", (bb) -> Minecraft.getInstance().setScreen(new TitleScreen()), Identifier.withDefaultNamespace("textures/item/arrow.png"));
      this.addRenderableWidget(backToMainButton);
   }

   public void tick() {
      super.tick();
      this.queueCompetitiveButton.active = !LockoutMatchData.isInMatch();
      this.queueQuickPlayButton.active = !LockoutMatchData.isInMatch();
      this.leaderboardButton.active = !LockoutMatchData.isInMatchWorld();
      this.settingsButton.active = !LockoutMatchData.isInMatchWorld();
      this.searchProfileButton.active = !LockoutMatchData.isInMatchWorld();
      this.queueCompetitiveButton.setTooltip(Tooltip.create(Component.empty().append(Component.literal("Competitive").withStyle(ChatFormatting.BOLD).withColor(RankedRank.Colors.DIAMOND_COLOR.getRGB())).append(Component.literal("\n\nRanked mode with stricter rules.\nDraft a 5x5 board and race to\nwin!\n\n")).append(Component.literal(ServerConnection.getInstance().queueType != MatchType.COMPETITIVE ? "Click to join queue." : "Click to leave queue").withStyle(ChatFormatting.UNDERLINE).withColor(RankedRank.Colors.DIAMOND_COLOR.getRGB()))));
      this.queueQuickPlayButton.setTooltip(Tooltip.create(Component.empty().append(Component.literal("Quick Play").withStyle(ChatFormatting.BOLD).withColor(RankedRank.Colors.DIAMOND_COLOR.getRGB())).append(Component.literal("\n\nClassic 5x5 Lockout experience.\nRace your opponent to 13 goals!\n\n")).append(Component.literal(ServerConnection.getInstance().queueType != MatchType.QUICK_PLAY ? "Click to join queue." : "Click to leave queue").withStyle(ChatFormatting.UNDERLINE).withColor(RankedRank.Colors.DIAMOND_COLOR.getRGB()))));
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      int width = 36 + (int)((float)this.font.width(titleComponent) * 3.0F);
      RankedGuiUtils.scaledText(graphics, titleComponent, graphics.guiWidth() / 2 - width / 2 + 32 + 4, 12, 3.0F, -1);
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("draftout", "recovery_compass_16"), graphics.guiWidth() / 2 - width / 2, 8, 32, 32, -1);
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }
}
