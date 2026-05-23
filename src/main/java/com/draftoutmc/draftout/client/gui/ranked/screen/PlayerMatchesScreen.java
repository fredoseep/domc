package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.client.gui.ranked.elements.MatchDataWidget;
import com.draftoutmc.draftout.client.gui.ranked.elements.MatchList;
import com.draftoutmc.draftout.client.gui.ranked.elements.MatchListEntry;
import com.draftoutmc.draftout.match.data.MatchHistoryData;
import com.draftoutmc.draftout.match.data.PlayerProfile;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.NonNull;

public class PlayerMatchesScreen extends Screen {
   private static final Component TITLE = Component.empty();
   private static final int buttonHeight = 20;
   private static final int matchListWidth = 170;
   private static int widgetY = 44;
   private MatchHistoryData.MatchPage currentPage;
   private PlayerProfile profile;
   private Button prevButton;
   private Button nextButton;
   private MatchList matchList;
   private MatchDataWidget matchDataWidget;
   private MatchListEntry clickedEntry;
   private final Consumer<MatchListEntry> onClick = (matchListEntry) -> {
      this.clickedEntry = matchListEntry;
      this.updateSelectedMatch();
   };

   public PlayerMatchesScreen(MatchHistoryData.MatchPage initialPage, PlayerProfile profile) {
      super(TITLE);
      this.currentPage = initialPage;
      this.profile = profile;
   }

   public void onPageReceived(MatchHistoryData.MatchPage page) {
      this.currentPage = page;
      this.rebuildList();
      this.updatePagination();
   }

   protected void init() {
      int buttonWidth = 20;
      int entryHeight = MatchListEntry.computeHeight();
      int widgetFillHeight = this.height - 44 - 20 - 8;
      int widgetHeight = Math.min(entryHeight * 5 + 4, widgetFillHeight);
      boolean shouldCenter = this.shouldCenter();
      if (shouldCenter) {
         widgetY = (this.height - 8 - 20) / 2 - widgetHeight / 2 + 10;
      } else {
         widgetY = 44;
      }

      this.matchList = new MatchList(this.minecraft, 170, widgetHeight, widgetY, entryHeight);
      this.addRenderableWidget(this.matchList);
      this.matchDataWidget = new MatchDataWidget(this.profile, 170, widgetY, this.width - 170, widgetHeight);
      this.addRenderableWidget(this.matchDataWidget);
      this.updateSelectedMatch();
      int navY = widgetY - 20 - 4;
      this.prevButton = Button.builder(Component.literal("<"), (btn) -> this.requestPage(this.currentPage.page() - 1)).bounds(4, navY, buttonWidth, 20).build();
      this.nextButton = Button.builder(Component.literal(">"), (btn) -> this.requestPage(this.currentPage.page() + 1)).bounds(166 - buttonWidth, navY, buttonWidth, 20).build();
      this.addRenderableWidget(this.prevButton);
      this.addRenderableWidget(this.nextButton);
      this.addRenderableWidget(Button.builder(Component.literal("Back"), (btn) -> Minecraft.getInstance().setScreen(new LockoutMainScreen())).bounds(8, this.height - 4 - 20, 80, 20).build());
      this.rebuildList();
      this.updatePagination();
   }

   private boolean shouldCenter() {
      int widgetFillHeight = this.height - widgetY - 20 - 8;
      int widgetHeight = Math.min(MatchListEntry.computeHeight() * 5 + 4, widgetFillHeight);
      return widgetHeight < widgetFillHeight;
   }

   private void updateSelectedMatch() {
      if (this.clickedEntry != null) {
         this.matchDataWidget.setMatchResult(this.clickedEntry.getMatch(), this.currentPage.profiles());
      }

   }

   public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      Font font = Minecraft.getInstance().font;
      int buttonHeight = 20;
      int var10000 = widgetY - buttonHeight / 2 - 4;
      Objects.requireNonNull(font);
      int textY = var10000 - 9 / 2;
      var10000 = this.currentPage.page();
      String pageLabel = "Page " + var10000 + "/" + this.currentPage.totalPages();
      MutableComponent var10002 = Component.literal("Match History").withStyle(ChatFormatting.WHITE);
      Objects.requireNonNull(font);
      graphics.centeredText(font, var10002, 85, textY - 9 - 8, -1);
      graphics.centeredText(font, Component.literal(pageLabel).withStyle(ChatFormatting.GRAY), 85, textY, -1);
      if (this.currentPage.page() == 1 && this.currentPage.matches().isEmpty()) {
         graphics.centeredText(font, Component.literal("No matches yet...").withStyle(ChatFormatting.GRAY), 85, widgetY + this.matchList.getHeight() / 2, -1);
      }

   }

   public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   private void requestPage(int page) {
      this.prevButton.active = false;
      this.nextButton.active = false;
      ServerConnection.getInstance().sendMatchesRequest(this.currentPage.playerId(), page);
   }

   private void rebuildList() {
      this.matchList.clearEntries();

      for(MatchHistoryData.MatchResult match : this.currentPage.matches()) {
         this.matchList.addEntry(new MatchListEntry(match, this.currentPage.playerId(), this.currentPage.profiles(), this.onClick));
      }

      this.matchList.setScrollAmount((double)0.0F);
   }

   private void updatePagination() {
      this.prevButton.active = this.currentPage.page() > 1;
      this.nextButton.active = this.currentPage.page() < this.currentPage.totalPages();
   }
}
