package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.client.gui.ranked.elements.LeaderboardEntry;
import com.draftoutmc.draftout.client.gui.ranked.elements.LeaderboardList;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LeaderboardScreen extends Screen {
   private static final Component title = Component.empty();
   private final List<LockoutMatchData.LockoutMatchPlayer> players;
   private LeaderboardList leaderboardList;

   public LeaderboardScreen(List<LockoutMatchData.LockoutMatchPlayer> players) {
      super(title);
      this.players = players;
   }

   protected void init() {
      int buttonWidth = 100;
      int buttonHeight = 20;
      this.addRenderableWidget(Button.builder(Component.literal("Back"), (bb) -> Minecraft.getInstance().setScreen(new LockoutMainScreen())).bounds(8, this.height - 8 - buttonHeight, buttonWidth, buttonHeight).build());
      this.leaderboardList = new LeaderboardList(this.minecraft, this.width, this.height - 70, 35, 16);
      int rowIdx = 1;

      for(LockoutMatchData.LockoutMatchPlayer player : this.players) {
         this.leaderboardList.addEntry(new LeaderboardEntry(player.uuid(), player.rank(), rowIdx++, player.username(), player.elo(), player.skin()));
      }

      this.addRenderableWidget(this.leaderboardList);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      Font var10001 = this.font;
      int var10003 = this.width / 2;
      Objects.requireNonNull(this.font);
      graphics.centeredText(var10001, "Leaderboard", var10003, 17 - 9 / 2, -1);
      if (this.leaderboardList.children().isEmpty()) {
         var10001 = this.font;
         MutableComponent var10002 = Component.literal("No ranked players yet...").withStyle(ChatFormatting.GRAY);
         var10003 = this.width / 2;
         int var10004 = this.height / 2;
         Objects.requireNonNull(this.font);
         graphics.centeredText(var10001, var10002, var10003, var10004 - 9 / 2, -1);
      }

   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }
}
