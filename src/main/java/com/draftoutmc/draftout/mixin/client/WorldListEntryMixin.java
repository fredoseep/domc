package com.draftoutmc.draftout.mixin.client;

import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutInformationScreen;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.client.gui.screens.worldselection.WorldSelectionList$WorldListEntry"}
)
public class WorldListEntryMixin {
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
      }

   }
}
