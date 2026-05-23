package com.draftoutmc.draftout.mixin.client;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Window.class})
public class WindowMixin {
   @Inject(
      method = {"setGuiScale"},
      at = {@At("RETURN")}
   )
   private void onGuiScaleChanged(int guiScale, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         Utility.sendScreenDataForTwitchExtension();
      }

   }
}
