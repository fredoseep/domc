package com.draftoutmc.draftout.mixin.client.export;

import com.draftoutmc.draftout.client.export.GoalIconExportEnvironment;
import com.mojang.blaze3d.audio.DeviceList;
import com.mojang.blaze3d.audio.DeviceTracker;
import com.mojang.blaze3d.audio.Library;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Library.class})
public abstract class LibraryExportMixin {
   private static final DeviceTracker NOOP_DEVICE_TRACKER = new DeviceTracker() {
      public DeviceList currentDevices() {
         return DeviceList.EMPTY;
      }

      public void tick() {
      }

      public void forceRefresh() {
      }
   };

   @Inject(
      method = {"createDeviceTracker"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void draftout$createNoopAudioDeviceTrackerForGoalIconExport(CallbackInfoReturnable<DeviceTracker> cir) {
      if (GoalIconExportEnvironment.isExportMode()) {
         System.out.println("[DraftoutGoalIconExport] using noop audio device tracker for headless export");
         cir.setReturnValue(NOOP_DEVICE_TRACKER);
      }
   }
}
