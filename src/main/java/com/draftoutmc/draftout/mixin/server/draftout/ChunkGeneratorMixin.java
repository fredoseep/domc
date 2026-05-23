package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.standardize.SpawnLavaLakesData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChunkGenerator.class})
public class ChunkGeneratorMixin {
   @Inject(
      method = {"applyBiomeDecoration"},
      at = {@At("TAIL")}
   )
   private void onApplyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         ServerLevel serverLevel = level.getLevel();
         if (serverLevel.equals(serverLevel.getServer().overworld())) {
            SpawnLavaLakesData data = SpawnLavaLakesData.get(serverLevel);
            if (!data.done) {
               synchronized(data) {
                  if (data.candidates.isEmpty()) {
                     data.candidates = this.draftout$generateCandidates(serverLevel.getSeed(), 3, 10);
                     data.setDirty();
                  }
               }

               ChunkPos chunkPos = chunk.getPos();
               long seed = serverLevel.getSeed();

               for(int i = 0; i < data.candidates.size(); ++i) {
                  BlockPos candidate = (BlockPos)data.candidates.get(i);
                  int targetIndex = i / 5;
                  if (candidate.getX() >> 4 == chunkPos.x() && candidate.getZ() >> 4 == chunkPos.z()) {
                     synchronized(data) {
                        if (data.done) {
                           return;
                        }

                        if (!data.succeededTargets.contains(targetIndex)) {
                           if (!data.attemptedCandidates.contains(i)) {
                              data.attemptedCandidates.add(i);
                              if (this.draftout$chunkHasLava(chunk)) {
                                 data.setDirty();
                              } else {
                                 boolean tooClose = false;

                                 for(BlockPos placed : data.placedPositions) {
                                    if (placed.closerThan(candidate, (double)64.0F)) {
                                       tooClose = true;
                                       break;
                                    }
                                 }

                                 if (tooClose) {
                                    data.setDirty();
                                 } else {
                                    Optional<Holder.Reference<PlacedFeature>> featureHolder = serverLevel.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(MiscOverworldPlacements.LAKE_LAVA_SURFACE);
                                    if (featureHolder.isEmpty()) {
                                       return;
                                    }

                                    PlacedFeature feature = (PlacedFeature)((Holder.Reference)featureHolder.get()).value();
                                    int y = chunk.getHeight(Types.WORLD_SURFACE, candidate.getX() & 15, candidate.getZ() & 15);
                                    BlockPos pos = new BlockPos(candidate.getX(), y, candidate.getZ());
                                    boolean insideVillage = false;

                                    for(ResourceKey<Structure> villageKey : List.of(BuiltinStructures.VILLAGE_PLAINS, BuiltinStructures.VILLAGE_DESERT, BuiltinStructures.VILLAGE_SAVANNA, BuiltinStructures.VILLAGE_SNOWY, BuiltinStructures.VILLAGE_TAIGA)) {
                                       Structure villageStructure = (Structure)serverLevel.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(villageKey);
                                       if (villageStructure != null && structureManager.getStructureAt(pos, villageStructure).isValid()) {
                                          insideVillage = true;
                                          break;
                                       }
                                    }

                                    if (insideVillage) {
                                       data.setDirty();
                                    } else {
                                       RandomSource rng = RandomSource.create(seed ^ (long)i * -7046029254386353131L);
                                       if (((ConfiguredFeature)feature.feature().value()).place(level, serverLevel.getChunkSource().getGenerator(), rng, pos)) {
                                          data.succeededTargets.add(targetIndex);
                                          data.placedPositions.add(pos);
                                       }

                                       boolean allResolved = true;

                                       for(int t = 0; t < 3; ++t) {
                                          if (!data.succeededTargets.contains(t)) {
                                             boolean allAttemptsExhausted = true;

                                             for(int a = t * 5; a < t * 5 + 5; ++a) {
                                                if (!data.attemptedCandidates.contains(a)) {
                                                   allAttemptsExhausted = false;
                                                   break;
                                                }
                                             }

                                             if (!allAttemptsExhausted) {
                                                allResolved = false;
                                                break;
                                             }
                                          }
                                       }

                                       if (allResolved) {
                                          data.done = true;
                                       }

                                       data.setDirty();
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   @Unique
   private boolean draftout$chunkHasLava(ChunkAccess chunk) {
      ChunkPos pos = chunk.getPos();
      int step = 2;

      for(int x = pos.getMinBlockX(); x <= pos.getMaxBlockX(); x += step) {
         for(int z = pos.getMinBlockZ(); z <= pos.getMaxBlockZ(); z += step) {
            int surfaceY = chunk.getHeight(Types.WORLD_SURFACE, x & 15, z & 15);

            for(int dy = -10; dy <= 1; ++dy) {
               FluidState fluid = chunk.getFluidState(new BlockPos(x, surfaceY + dy, z));
               if (fluid.getType() == Fluids.LAVA && fluid.isSource()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Unique
   private List<BlockPos> draftout$generateCandidates(long seed, int targets, int attemptsEach) {
      Random rng = new Random(seed ^ 209893959515877L);
      List<BlockPos> list = new ArrayList();

      for(int i = 0; i < targets * attemptsEach; ++i) {
         double angle = rng.nextDouble() * (double)2.0F * Math.PI;
         int dist = 100 + rng.nextInt(151);
         int x = (int)(Math.cos(angle) * (double)dist);
         int z = (int)(Math.sin(angle) * (double)dist);
         list.add(new BlockPos(x, 0, z));
      }

      return list;
   }
}
