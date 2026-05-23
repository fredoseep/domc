package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.anger.AngerZombifiedPiglinGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ZombifiedPiglin.class})
public class ZombifiedPiglinEntityMixin {
   @Inject(
      method = {"setPersistentAngerTarget"},
      at = {@At("HEAD")}
   )
   public void setAngryAt(@Nullable EntityReference<LivingEntity> persistentAngerTarget, CallbackInfo ci) {
      ZombifiedPiglin pigman = (ZombifiedPiglin)(Object)this;
      if (!pigman.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            ServerPlayer player;
            try {
               UUID angryAt = persistentAngerTarget.getUUID();
               player = pigman.level().getServer().getPlayerList().getPlayer(angryAt);
               if (player == null) {
                  return;
               }
            } catch (Exception var8) {
               return;
            }

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && goal instanceof AngerZombifiedPiglinGoal && !goal.isCompleted()) {
                  lockout.completeGoal(goal, (Player)player);
                  return;
               }
            }

         }
      }
   }
}
