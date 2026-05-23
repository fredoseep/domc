package com.draftoutmc.draftout.mixin.client;

import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutWaitingScreen;
import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.MinecraftUtils;
import com.draftoutmc.draftout.match.RankedUtils;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Minecraft.class})
public class MinecraftMixin {
   @Inject(
      method = {"pauseGame"},
      at = {@At("HEAD")}
   )
   private void onPauseGame(boolean suppressPauseMenuIfWeReallyArePausing, CallbackInfo ci) {
      if (RankedUtils.isInDraftPhase() && Minecraft.getInstance().level != null && Minecraft.getInstance().screen == null) {
         Minecraft.getInstance().setScreen(LockoutMatchData.CURRENT_MATCH.draftScreen());
      }

   }

   @Redirect(
      method = {"pauseIfInactive"},
      at = @At(
   value = "FIELD",
   target = "Lnet/minecraft/client/Options;pauseOnLostFocus:Z",
   opcode = 180
)
   )
   private boolean redirectPauseOnLostFocus(Options options) {
      if (((Minecraft)(Object)this).screen instanceof LevelLoadingScreen) {
         return false;
      } else if (((Minecraft)(Object)this).screen instanceof LockoutWaitingScreen) {
         return false;
      } else if (LockoutMatchData.isInMatch() && LockoutMatchData.CURRENT_MATCH.matchType() == MatchType.COMPETITIVE && !LockoutMatchData.CURRENT_MATCH.draftBegun() && ((Minecraft)(Object)this).screen == null) {
         return false;
      } else {
         return LockoutMatchData.isInMatch() && LockoutMatchData.CURRENT_MATCH.matchType() == MatchType.COMPETITIVE && LockoutMatchData.getLockout() != null && LockoutMatchData.getLockout().hasStarted() && !LockoutMatchData.getLockout().isRunning() && ((Minecraft)(Object)this).screen == null ? false : options.pauseOnLostFocus;
      }
   }

   @Inject(
      method = {"disconnectFromWorld"},
      at = {@At("HEAD")}
   )
   private void onDisconnectStart(CallbackInfo ci) {
      MinecraftUtils.onMinecraftDisconnectStart();
   }

   @Inject(
      method = {"disconnectFromWorld"},
      at = {@At("TAIL")}
   )
   private void onDisconnectEnd(CallbackInfo ci) {
      MinecraftUtils.onMinecraftDisconnectEnd();
   }
}
