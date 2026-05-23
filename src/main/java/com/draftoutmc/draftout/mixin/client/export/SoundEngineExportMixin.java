package com.draftoutmc.draftout.mixin.client.export;

import com.draftoutmc.draftout.client.export.GoalIconExportEnvironment;
import com.mojang.blaze3d.audio.DeviceList;
import com.mojang.blaze3d.audio.DeviceTracker;
import com.mojang.blaze3d.audio.Library;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({SoundEngine.class})
public abstract class SoundEngineExportMixin {
   private static final DeviceTracker NOOP_DEVICE_TRACKER = new DeviceTracker() {
      public DeviceList currentDevices() {
         return DeviceList.EMPTY;
      }

      public void tick() {
      }

      public void forceRefresh() {
      }
   };

   @Redirect(
      method = {"<init>"},
      at = @At(
   value = "INVOKE",
   target = "Lcom/mojang/blaze3d/audio/Library;createDeviceTracker()Lcom/mojang/blaze3d/audio/DeviceTracker;"
)
   )
   private DeviceTracker draftout$createNoopAudioDeviceTrackerForGoalIconExport() {
      if (!GoalIconExportEnvironment.isExportMode()) {
         return Library.createDeviceTracker();
      } else {
         System.out.println("[DraftoutGoalIconExport] using noop sound engine device tracker for headless export");
         return NOOP_DEVICE_TRACKER;
      }
   }
}
