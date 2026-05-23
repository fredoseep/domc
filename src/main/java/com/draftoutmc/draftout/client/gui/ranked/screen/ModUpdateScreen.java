package com.draftoutmc.draftout.client.gui.ranked.screen;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;

public class ModUpdateScreen extends Screen {
   private Button updateButton;
   private Button closeButton;
   private final Component message;
   private final String downloadURL;
   private boolean downloading = false;
   private boolean downloaded = false;
   private int downloadProgress = 0;

   public ModUpdateScreen(String message, String downloadURL) {
      super(Component.empty());
      this.message = Component.literal(message);
      this.downloadURL = downloadURL;
   }

   protected void init() {
      super.init();
      int width = 200;
      int height = 20;
      this.updateButton = (Button)this.addRenderableWidget(Button.builder(Component.literal("Update now"), (bb) -> {
         this.downloadProgress = 0;
         this.downloading = true;
         CompletableFuture.runAsync(() -> {
            try {
               Utility.downloadUpdate(this.downloadURL, (progress) -> this.downloadProgress = progress);
               this.downloaded = true;
            } catch (Exception e) {
               Lockout.error("Failed to update", e);
            } finally {
               this.downloading = false;
            }

         });
      }).bounds(this.width / 2 - width / 2, this.height / 2 - height / 2 + 30, width, height).build());
      this.closeButton = (Button)this.addRenderableWidget(Button.builder(Component.literal("Close game"), (bb) -> Minecraft.getInstance().stop()).bounds(this.width / 2 - width / 2, this.height / 2 - height / 2 + 30, width, height).build());
   }

   public void tick() {
      super.tick();
      this.updateButton.active = !this.downloading && !this.downloaded;
      this.updateButton.visible = !this.downloaded;
      this.closeButton.visible = this.downloaded;
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      int textY = this.height / 2 - 30;
      int borderColor = ARGB.color(255, -6250336);
      graphics.centeredText(this.font, "Update Draftout", this.width / 2, 8, -1);
      if (!this.downloading && !this.downloaded) {
         graphics.centeredText(this.font, this.message, this.width / 2, textY, -1);
         Font var13 = this.font;
         int var19 = this.width / 2;
         Objects.requireNonNull(this.font);
         graphics.centeredText(var13, "Click the button below to update.", var19, textY + 9 + 2, -1);
      } else if (!this.downloaded) {
         String text = "Downloading update...";
         int margin = 8;
         int textWidth = this.font.width(text);
         int textX = this.width / 2;
         int var10001 = textX - textWidth / 2 - margin;
         Objects.requireNonNull(this.font);
         int var10002 = textY - 9 / 2 - margin;
         int var10003 = textX + textWidth / 2 + margin;
         Objects.requireNonNull(this.font);
         graphics.fill(var10001, var10002, var10003, textY + 9 / 2 + margin, ChatFormatting.DARK_GRAY.getColor());
         var10001 = textX - textWidth / 2 - margin;
         Objects.requireNonNull(this.font);
         var10002 = textY - 9 / 2 - margin;
         var10003 = textWidth + margin * 2;
         Objects.requireNonNull(this.font);
         graphics.outline(var10001, var10002, var10003, 9 + margin * 2, borderColor);
         Font var12 = this.font;
         MutableComponent var15 = Component.literal(text);
         var10003 = this.width / 2;
         Objects.requireNonNull(this.font);
         graphics.centeredText(var12, var15, var10003, textY - 9 / 2, -1);
         int var16 = textX - textWidth / 2 - margin;
         Objects.requireNonNull(this.font);
         this.drawProgressBar(graphics, var16, textY + 9 / 2 + margin + 1, textWidth + margin * 2, 1, (float)this.downloadProgress / 100.0F);
      } else {
         graphics.centeredText(this.font, "Downloaded update! Restart your game.", this.width / 2, textY, -1);
      }

      super.extractRenderState(graphics, mouseX, mouseY, a);
   }

   private void drawProgressBar(GuiGraphicsExtractor graphics, int left, int top, int width, int height, float progress) {
      graphics.fill(left, top, left + width, top + height, -16777216);
      graphics.fill(left, top, left + Math.round(progress * (float)width), top + height, -16711936);
   }

   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
      RankedGuiUtils.extractBackground(graphics, this.width, this.height);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}
