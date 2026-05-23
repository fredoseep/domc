package com.draftoutmc.draftout.mixin.client.gui;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutConfig;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.gui.ranked.elements.QueueInfoRenderer;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Gui.class})
public abstract class GuiMixin {
   @Final
   @Shadow
   private PlayerTabOverlay tabList;
   @Final
   @Shadow
   private Minecraft minecraft;

   @Shadow
   public abstract void extractRenderState(GuiGraphicsExtractor var1, DeltaTracker var2);

   @Inject(
      method = {"extractRenderState"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/Gui;extractSleepOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V",
   shift = Shift.AFTER
)}
   )
   public void renderBoard(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      if (!this.minecraft.debugEntries.isOverlayVisible()) {
         if (Lockout.exists(LockoutMatchData.getLockout())) {
            Lockout lockout = LockoutMatchData.getLockout();
            Utility.drawBoardOverlay(graphics, lockout.getBoard(), lockout.getTeams(), Utility.msToTimer(lockout.getTimer(), 0));
         }
      }
   }

   @Redirect(
      method = {"extractEffects"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiWidth()I"
)
   )
   private int renderStatusEffects(GuiGraphicsExtractor instance) {
      int width = instance.guiWidth();
      if (this.minecraft.debugEntries.isOverlayVisible()) {
         return width;
      } else if (!Lockout.exists(LockoutMatchData.getLockout())) {
         return width;
      } else {
         return LockoutConfig.getInstance().boardPosition != LockoutConfig.BoardPosition.RIGHT ? width : width - 4 - LockoutMatchData.getLockout().getBoard().size() * 18;
      }
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("TAIL")}
   )
   public void renderQueueInfo(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      QueueInfoRenderer.render(graphics);
   }

   @Inject(
      method = {"extractTabList"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onExtractTabList(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         ci.cancel();
         Scoreboard scoreboard = this.minecraft.level.getScoreboard();
         Objective displayObjective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
         if (!this.minecraft.options.keyPlayerList.isDown()) {
            this.tabList.setVisible(false);
         } else {
            this.tabList.setVisible(true);
            graphics.nextStratum();
            this.tabList.extractRenderState(graphics, graphics.guiWidth(), scoreboard, displayObjective);
         }
      }
   }
}
