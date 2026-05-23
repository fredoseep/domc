package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerSpawnFinder.class})
public class PlayerSpawnFinderMixin {
   @Final
   @Shadow
   @Mutable
   private int offset;
   @Shadow
   @Final
   private int candidateCount;
   @Shadow
   @Final
   private ServerLevel level;
   @Unique
   private static final int MAX_SPIRAL_SEARCH = 64;

   @Redirect(
      method = {"<init>"},
      at = @At(
   value = "FIELD",
   target = "Lnet/minecraft/server/level/PlayerSpawnFinder;offset:I",
   opcode = 181
)
   )
   private void init(PlayerSpawnFinder instance, int value) {
      if (LockoutMatchData.isInMatch()) {
         int count = this.candidateCount > 0 ? this.candidateCount : 1;
         this.offset = RandomSource.createThreadLocalInstance(this.level.getSeed()).nextInt(count);
      }
   }

   @Inject(
      method = {"getOverworldRespawnPos"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void avoidSpawnBlocks(ServerLevel level, int x, int z, CallbackInfoReturnable<BlockPos> cir) {
      if (LockoutMatchData.isInMatch()) {
         BlockPos first = (BlockPos)cir.getReturnValue();
         if (!lockout$isSafe(level, first)) {
            for(BlockPos.MutableBlockPos spiral : BlockPos.spiralAround(new BlockPos(x, 0, z), 64, Direction.EAST, Direction.SOUTH)) {
               int testX = spiral.getX();
               int testZ = spiral.getZ();
               if (level.getWorldBorder().isWithinBounds((double)testX, (double)testZ)) {
                  BlockPos candidate = lockout$findOverworldRespawnPosVanilla(level, testX, testZ);
                  if (lockout$isSafe(level, candidate)) {
                     cir.setReturnValue(candidate);
                     return;
                  }
               }
            }

            cir.setReturnValue(first);
         }
      }
   }

   @Unique
   private static boolean lockout$isSafe(ServerLevel level, @Nullable BlockPos pos) {
      if (pos == null) {
         return false;
      } else {
         BlockPos head = pos.above();
         BlockPos below = pos.below();
         BlockState feetState = level.getBlockState(pos);
         BlockState headState = level.getBlockState(head);
         BlockState belowState = level.getBlockState(below);
         if (!feetState.is(Blocks.POWDER_SNOW) && !headState.is(Blocks.POWDER_SNOW) && !belowState.is(Blocks.POWDER_SNOW)) {
            if (!feetState.is(BlockTags.LEAVES) && !headState.is(BlockTags.LEAVES) && !belowState.is(BlockTags.LEAVES)) {
               if (feetState.getFluidState().isEmpty() && headState.getFluidState().isEmpty()) {
                  return pos.getY() >= 40;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Unique
   private static @Nullable BlockPos lockout$findOverworldRespawnPosVanilla(ServerLevel level, int x, int z) {
      boolean caveWorld = level.dimensionType().hasCeiling();
      LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
      int topY = caveWorld ? level.getChunkSource().getGenerator().getSpawnHeight(level) : chunk.getHeight(Types.MOTION_BLOCKING, x & 15, z & 15);
      if (topY < level.getMinY()) {
         return null;
      } else {
         int surface = chunk.getHeight(Types.WORLD_SURFACE, x & 15, z & 15);
         if (surface <= topY && surface > chunk.getHeight(Types.OCEAN_FLOOR, x & 15, z & 15)) {
            return null;
         } else {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for(int y = topY + 1; y >= level.getMinY(); --y) {
               pos.set(x, y, z);
               BlockState blockState = level.getBlockState(pos);
               if (!blockState.getFluidState().isEmpty()) {
                  break;
               }

               if (Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP)) {
                  return pos.above().immutable();
               }
            }

            return null;
         }
      }
   }
}
