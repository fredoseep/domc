package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutWaitingScreen;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.awt.Color;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class LeaderboardEntry extends AbstractSelectionList.Entry<LeaderboardEntry> {
   private static final int PADDING_RIGHT = 10;
   private static final Color ODD_BACKGROUND = new Color(234881023, true);
   private static final Color EVEN_BACKGROUND = new Color(419430400, true);
   private final UUID uuid;
   private final String playerName;
   private final int elo;
   private final int rank;
   private final int rowIdx;
   private final Identifier skin;

   public LeaderboardEntry(UUID uuid, int rank, int rowIdx, String playerName, int elo, Identifier skin) {
      this.playerName = playerName;
      this.elo = elo;
      this.rowIdx = rowIdx;
      this.rank = rank;
      this.skin = skin;
      this.uuid = uuid;
   }

   public int getWidth() {
      return super.getWidth() - 10;
   }

   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float alpha) {
      int x = this.getContentX();
      int y = this.getContentY();
      Font font = Minecraft.getInstance().font;
      int var10000 = this.getContentYMiddle();
      Objects.requireNonNull(font);
      int centerY = var10000 - 9 / 2;
      int ranksWidth = font.width("#100");
      Color bg;
      if (this.rowIdx % 2 == 0) {
         bg = EVEN_BACKGROUND;
      } else {
         bg = ODD_BACKGROUND;
      }

      RankedGuiUtils.rightToLeftGradient(graphics, this.getX() - 12, this.getY(), this.getX(), this.getY() + this.getHeight(), bg.getRGB(), bg.getRGB() & 16777215);
      graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bg.getRGB());
      RankedGuiUtils.rightToLeftGradient(graphics, this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth() + 12, this.getY() + this.getHeight(), bg.getRGB() & 16777215, bg.getRGB());
      if (hovered) {
         int overlay = 419430399;
         RankedGuiUtils.rightToLeftGradient(graphics, this.getX() - 12, this.getY(), this.getX(), this.getY() + this.getHeight(), overlay, overlay & 16777215);
         graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), overlay);
         RankedGuiUtils.rightToLeftGradient(graphics, this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth() + 12, this.getY() + this.getHeight(), overlay & 16777215, overlay);
      }

      Component rankComponent = RankedGuiUtils.getPlaceComponent(this.rank);
      graphics.text(font, rankComponent, x + ranksWidth - font.width(rankComponent.getString()), centerY, -1);
      RankedGuiUtils.renderHead(graphics, this.skin, x + ranksWidth + 4, y + 1, 10);
      graphics.text(font, this.playerName, x + ranksWidth + 18, centerY, -1);
      Component eloComponent = RankedGuiUtils.getEloComponent(this.elo);
      graphics.text(font, eloComponent, this.getContentRight() - font.width(eloComponent.getString()), centerY, -1);
   }

   public boolean isMouseOver(double mouseX, double mouseY) {
      return mouseX >= (double)this.getX() && mouseX <= (double)(this.getX() + this.getWidth()) && mouseY >= (double)this.getY() && mouseY <= (double)(this.getY() + this.getHeight());
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      if (event.button() == 0) {
         Minecraft mc = Minecraft.getInstance();
         ServerConnection.getInstance().setPreviousScreen();
         ServerConnection.getInstance().sendProfileRequest(this.uuid);
         mc.setScreen(new LockoutWaitingScreen("Loading profile..."));
         return true;
      } else {
         return false;
      }
   }
}
