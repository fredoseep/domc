package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.LeashMobGoal;
import com.draftoutmc.draftout.lockout.interfaces.LeashUniqueMobsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Leashable.class})
public interface LeashableMixin {
   @Inject(
      method = {"setLeashedTo(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Z)V"},
      at = {@At("TAIL")}
   )
   private static <E extends Entity & Leashable> void onSetLeashedTo(E entity, Entity holder, boolean synch, CallbackInfo ci) {
      if (holder instanceof Player player) {
         if (!player.level().isClientSide()) {
            Lockout lockout = LockoutMatchData.getLockout();
            if (Lockout.isLockoutRunning(lockout)) {
               List<Leashable> allLeashed = Leashable.leashableLeashedTo(player);
               Set<EntityType<?>> mobs = new HashSet();

               for(Leashable leashed : allLeashed) {
                  if (leashed instanceof Mob) {
                     Mob mob = (Mob)leashed;
                     mobs.add(mob.getType());
                  }
               }

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted()) {
                     if (goal instanceof LeashUniqueMobsGoal) {
                        LeashUniqueMobsGoal leashUniqueMobsGoal = (LeashUniqueMobsGoal)goal;
                        if (mobs.size() >= leashUniqueMobsGoal.getAmount()) {
                           lockout.completeGoal(goal, player);
                        }
                     }

                     if (goal instanceof LeashMobGoal) {
                        LeashMobGoal leashMobGoal = (LeashMobGoal)goal;
                        if (mobs.contains(leashMobGoal.getMob())) {
                           lockout.completeGoal(goal, player);
                        }
                     }
                  }
               }

            }
         }
      }
   }
}
