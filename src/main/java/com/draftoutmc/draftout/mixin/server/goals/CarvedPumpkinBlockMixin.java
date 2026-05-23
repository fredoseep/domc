package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.MixinContext;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.ConstructCopperGolemGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CarvedPumpkinBlock.class})
public class CarvedPumpkinBlockMixin {
   @Inject(
      method = {"trySpawnGolem"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/animal/golem/CopperGolem;spawn(Lnet/minecraft/world/level/block/WeatheringCopper$WeatherState;)V"
)}
   )
   private void onCopperGolemSpawn(Level level, BlockPos topPos, CallbackInfo ci) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            ServerPlayer player = (ServerPlayer)MixinContext.PUMPKIN_CARVING_PLAYER.get();
            if (player == null) {
               player = (ServerPlayer)MixinContext.BLOCK_PLACING_PLAYER.get();
            }

            if (player != null) {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof ConstructCopperGolemGoal) {
                     lockout.completeGoal(goal, (Player)player);
                  }
               }

            }
         }
      }
   }
}
