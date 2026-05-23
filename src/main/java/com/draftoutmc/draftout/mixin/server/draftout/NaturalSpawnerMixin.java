package com.draftoutmc.draftout.mixin.server.draftout;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NaturalSpawner.class})
public class NaturalSpawnerMixin {
   @Inject(
      method = {"isValidSpawnPostitionForType"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void preventHostileSpawnInDesertPyramid(ServerLevel level, MobCategory mobCategory, StructureManager structureManager, ChunkGenerator generator, MobSpawnSettings.SpawnerData currentSpawnData, BlockPos.MutableBlockPos pos, double nearestPlayerDistanceSqr, CallbackInfoReturnable<Boolean> cir) {
      if ((Boolean)cir.getReturnValue() && mobCategory == MobCategory.MONSTER) {
         Structure pyramid = (Structure)structureManager.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BuiltinStructures.DESERT_PYRAMID);
         if (pyramid != null && structureManager.getStructureAt(pos, pyramid).isValid()) {
            cir.setReturnValue(false);
         }

      }
   }
}
