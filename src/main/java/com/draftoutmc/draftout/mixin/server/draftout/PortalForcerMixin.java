package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.standardize.SurfacePortalForcing;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.BlockUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PortalForcer.class})
public abstract class PortalForcerMixin {
   @Shadow
   @Final
   private ServerLevel level;

   @ModifyVariable(
      method = {"createPortal"},
      at = @At("STORE"),
      name = {"closestFullPosition"}
   )
   private BlockPos forceSurfacePortalPos1(BlockPos closestFullPosition) {
      return this.logic(closestFullPosition);
   }

   @Inject(
      method = {"createPortal"},
      at = {@At("TAIL")}
   )
   private void addOceanPlatform(BlockPos origin, Direction.Axis portalAxis, CallbackInfoReturnable<Optional<BlockUtil.FoundRectangle>> cir, @Local(name = {"closestFullPosition"}) BlockPos closestFullPosition) {
      if (SurfacePortalForcing.isActive() && closestFullPosition != null) {
         boolean waterBiome = this.level.getBiome(closestFullPosition.atY(63)).is(BiomeTags.IS_OCEAN) || this.level.getBiome(closestFullPosition.atY(63)).is(BiomeTags.IS_DEEP_OCEAN) || this.level.getBiome(closestFullPosition.atY(63)).is(BiomeTags.IS_RIVER);
         if (waterBiome) {
            Direction direction = Direction.get(AxisDirection.POSITIVE, portalAxis);
            Direction clockWise = direction.getClockWise();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for(int box = -1; box < 2; ++box) {
               for(int width = 0; width < 2; ++width) {
                  mutable.setWithOffset(closestFullPosition, width * direction.getStepX() + box * clockWise.getStepX(), -1, width * direction.getStepZ() + box * clockWise.getStepZ());
                  this.level.setBlock(mutable, Blocks.OBSIDIAN.defaultBlockState(), 3);
               }
            }

         }
      }
   }

   @Unique
   private BlockPos logic(BlockPos closestFullPosition) {
      if (SurfacePortalForcing.isActive() && closestFullPosition != null) {
         int x = closestFullPosition.getX();
         int z = closestFullPosition.getZ();
         this.level.getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);
         if (this.isBlindAboveWater(closestFullPosition)) {
            return closestFullPosition.atY(70);
         } else {
            int surfaceY = this.level.getHeight(Types.MOTION_BLOCKING, x, z);
            if (Math.abs(closestFullPosition.getY() - surfaceY) <= 5) {
               return closestFullPosition;
            } else {
               int safeY = Math.min(surfaceY, this.level.getMaxY() - 4);
               return closestFullPosition.atY(safeY);
            }
         }
      } else {
         return closestFullPosition;
      }
   }

   @Unique
   private boolean isBlindAboveWater(BlockPos blockPos) {
      return this.level.getBiome(blockPos.atY(63)).is(BiomeTags.IS_OCEAN) || this.level.getBiome(blockPos.atY(63)).is(BiomeTags.IS_DEEP_OCEAN) || this.level.getBiome(blockPos.atY(63)).is(BiomeTags.IS_RIVER);
   }
}
