package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.standardize.SurfacePortalForcing;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NetherPortalBlock.class})
public abstract class NetherPortalBlockMixin {
   @Inject(
      method = {"getExitPortal"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/portal/PortalForcer;createPortal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction$Axis;)Ljava/util/Optional;"
)}
   )
   private void beforeCreatePortal(ServerLevel newLevel, Entity entity, BlockPos portalEntryPos, BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder, CallbackInfoReturnable<TeleportTransition> cir) {
      if (!toNether && entity instanceof Player && !SurfacePortalForcing.hasExited() && approximateExitPos.getY() >= 55 && LockoutMatchData.isInMatch()) {
         SurfacePortalForcing.activate();
      }

   }

   @Inject(
      method = {"getExitPortal"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/portal/PortalForcer;createPortal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction$Axis;)Ljava/util/Optional;",
   shift = Shift.AFTER
)}
   )
   private void afterCreatePortal(ServerLevel newLevel, Entity entity, BlockPos portalEntryPos, BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder, CallbackInfoReturnable<TeleportTransition> cir) {
      if (SurfacePortalForcing.isActive()) {
         SurfacePortalForcing.deactivate();
         if (entity instanceof Player) {
            SurfacePortalForcing.markExited();
         }
      }

   }
}
