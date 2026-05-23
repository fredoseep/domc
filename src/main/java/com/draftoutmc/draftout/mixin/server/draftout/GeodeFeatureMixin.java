package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GeodeFeature.class})
public abstract class GeodeFeatureMixin extends Feature<GeodeConfiguration> {
   public GeodeFeatureMixin(Codec<GeodeConfiguration> codec) {
      super(codec);
   }

   @Inject(
      method = {"place"},
      at = {@At("RETURN")}
   )
   private void guaranteeFullGrownCluster(FeaturePlaceContext<GeodeConfiguration> context, CallbackInfoReturnable<Boolean> cir, @Local(index = 30) List<BlockPos> potentialCrystalPlacements) {
      if (LockoutMatchData.isInMatch()) {
         if (!potentialCrystalPlacements.isEmpty()) {
            for(BlockPos buddingPos : potentialCrystalPlacements) {
               for(Direction dir : Direction.values()) {
                  BlockPos clusterPos = buddingPos.relative(dir);
                  BlockState existing = context.level().getBlockState(clusterPos);
                  if (BuddingAmethystBlock.canClusterGrowAtState(existing)) {
                     BlockState cluster = (BlockState)((BlockState)Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, dir)).setValue(AmethystClusterBlock.WATERLOGGED, existing.getFluidState().is(Fluids.WATER));
                     context.level().setBlock(clusterPos, cluster, 3);
                     return;
                  }
               }
            }

         }
      }
   }
}
