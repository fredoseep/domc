package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.UseGoldenDandelionGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AgeableMob.class})
public class AgeableMobMixin {
   @Inject(
      method = {"setAgeLocked(Lnet/minecraft/world/entity/Mob;Ljava/util/function/Supplier;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)V"},
      at = {@At("TAIL")}
   )
   private static void onSetAgeLocked(Mob mob, Supplier<Boolean> isAgedLocked, Player player, ItemStack itemInHand, Consumer<Mob> setAgeLockData, CallbackInfo ci) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            if ((Boolean)isAgedLocked.get()) {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof UseGoldenDandelionGoal) {
                     lockout.completeGoal(goal, player);
                  }
               }

            }
         }
      }
   }
}
