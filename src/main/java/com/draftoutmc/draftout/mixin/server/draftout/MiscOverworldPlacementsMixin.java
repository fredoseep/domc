package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({MiscOverworldPlacements.class})
public class MiscOverworldPlacementsMixin {
   @ModifyArg(
      method = {"bootstrap"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/levelgen/placement/RarityFilter;onAverageOnceEvery(I)Lnet/minecraft/world/level/levelgen/placement/RarityFilter;",
   ordinal = 3
),
      index = 0
   )
   private static int modifyLavaLakeSurfaceRarity(int original) {
      return LockoutMatchData.isInMatch() ? 150 : original;
   }
}
