package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Level.class})
public class LevelMixin {
   @Redirect(
      method = {"tickBlockEntities"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/TickRateManager;runsNormally()Z"
)
   )
   public boolean lockout$tickBlockEntities(TickRateManager instance) {
      Level level = (Level)(Object)this;
      return LockoutMatchData.isInMatch() && level.tickRateManager().isFrozen() ? true : instance.runsNormally();
   }
}
