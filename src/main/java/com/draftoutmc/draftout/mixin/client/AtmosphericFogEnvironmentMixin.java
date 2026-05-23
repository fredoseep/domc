package com.draftoutmc.draftout.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AtmosphericFogEnvironment.class})
public class AtmosphericFogEnvironmentMixin {
   @Inject(
      method = {"setupFog"},
      at = {@At("TAIL")}
   )
   private void modifyNetherFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
      if (level.dimension() == Level.NETHER) {
         fog.environmentalStart = 48.0F;
         fog.environmentalEnd = 192.0F;
      }

   }
}
