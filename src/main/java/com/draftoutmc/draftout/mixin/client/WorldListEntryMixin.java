package com.draftoutmc.draftout.mixin.client;

import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutInformationScreen;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.RankedData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.client.gui.screens.worldselection.WorldSelectionList$WorldListEntry"}
)
public class WorldListEntryMixin {
   @Shadow
   @Final
   private LevelSummary summary;

   @Inject(
      method = {"joinWorld"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void lockout$preventJoiningWorld(CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
         Minecraft.getInstance().setScreen(new LockoutInformationScreen("Can't join worlds during active matches.", Component.literal("Warning")));
         ci.cancel();
      } else if (!ServerConnection.connected() && this.summary.getLevelId().equals(RankedData.worldName())) {
         Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
         Minecraft.getInstance().setScreen(new LockoutInformationScreen("Can't join active match world while disconnected.", Component.literal("Warning")));
         ci.cancel();
      }

   }
}
