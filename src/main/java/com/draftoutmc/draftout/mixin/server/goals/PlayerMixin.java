package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.HaveShieldDisabledGoal;
import com.draftoutmc.draftout.lockout.goals.misc.Sprint1KmGoal;
import com.draftoutmc.draftout.lockout.goals.misc.Take200DamageGoal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentEatsFoodGoal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentTakes100DamageGoal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentTakesFallDamageGoal;
import com.draftoutmc.draftout.lockout.interfaces.IncrementStatGoal;
import com.draftoutmc.draftout.lockout.interfaces.ReachXPLevelGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public abstract class PlayerMixin {
   @Inject(
      method = {"hurtServer"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onDamageBeforeMatch(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (!level.isClientSide()) {
            if (!lockout.hasStarted()) {
               cir.setReturnValue(false);
            }

         }
      }
   }

   @Inject(
      method = {"attack"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onAttackBeforeMatch(Entity entity, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (!lockout.hasStarted()) {
            ci.cancel();
         }

      }
   }

   @Inject(
      method = {"hurtServer"},
      at = {@At("RETURN")}
   )
   public void onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if ((Boolean)cir.getReturnValue()) {
            Player player = (Player)(Object)this;
            if (!player.level().isClientSide()) {
               if (lockout.isLockoutPlayer(player.getUUID())) {
                  LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());
                  lockout.damageTaken.putIfAbsent(team, (double)0.0F);
                  lockout.damageTaken.merge(team, (double)amount, Double::sum);

                  for(Goal goal : lockout.getBoard().getGoals()) {
                     if (goal != null && !goal.isCompleted()) {
                        if (goal instanceof Take200DamageGoal) {
                           Take200DamageGoal take200DamageGoal = (Take200DamageGoal)goal;
                           team.sendTooltipUpdate(take200DamageGoal);
                           if ((Double)lockout.damageTaken.get(team) >= (double)200.0F) {
                              lockout.completeGoal(goal, (LockoutTeam)team);
                           }
                        }

                        if (goal instanceof OpponentTakesFallDamageGoal && source.is(DamageTypes.FALL)) {
                           lockout.complete1v1Goal(goal, player, false, player.getName().getString() + " took fall damage.");
                        }

                        if (goal instanceof OpponentTakes100DamageGoal && (Double)lockout.damageTaken.get(team) >= (double)100.0F) {
                           lockout.complete1v1Goal(goal, (LockoutTeam)team, false, team.getDisplayName() + " took 100 damage.");
                        }
                     }
                  }

               }
            }
         }
      }
   }

   @Inject(
      method = {"awardStat(Lnet/minecraft/resources/Identifier;)V"},
      at = {@At("HEAD")}
   )
   public void onIncrementStat(Identifier stat, CallbackInfo ci) {
      Player player = (Player)(Object)this;
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted()) {
                  if (goal instanceof IncrementStatGoal) {
                     IncrementStatGoal incrementStatGoal = (IncrementStatGoal)goal;
                     if (incrementStatGoal.getStats().contains(stat)) {
                        lockout.completeGoal(goal, player);
                     }
                  }

                  if (goal instanceof OpponentEatsFoodGoal && stat.equals(Stats.EAT_CAKE_SLICE)) {
                     lockout.complete1v1Goal(goal, player, false, player.getName().getString() + " ate food.");
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"awardStat(Lnet/minecraft/resources/Identifier;I)V"},
      at = {@At("HEAD")}
   )
   public void onIncreaseStat(Identifier stat, int amount, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         Player player = (Player)(Object)this;
         if (!player.level().isClientSide()) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof Sprint1KmGoal) {
                  Sprint1KmGoal sprint1KmGoal = (Sprint1KmGoal)goal;
                  if (stat.equals(Stats.SPRINT_ONE_CM)) {
                     LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());
                     lockout.distanceSprinted.putIfAbsent(player.getUUID(), 0);
                     lockout.distanceSprinted.merge(player.getUUID(), amount, Integer::sum);
                     team.sendTooltipUpdate(sprint1KmGoal);
                     if ((Integer)lockout.distanceSprinted.get(player.getUUID()) >= 100000) {
                        lockout.completeGoal(goal, player);
                     }
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"giveExperienceLevels"},
      at = {@At("TAIL")}
   )
   public void onExperienceLevelUp(int levels, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         Player player = (Player)(Object)this;
         if (!player.level().isClientSide()) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof ReachXPLevelGoal) {
                  ReachXPLevelGoal reachXPLevelGoal = (ReachXPLevelGoal)goal;
                  if (player.experienceLevel >= reachXPLevelGoal.getAmount()) {
                     lockout.completeGoal(goal, player);
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"blockUsingItem"},
      at = {@At("TAIL")}
   )
   public void onTakeShieldHit(ServerLevel world, LivingEntity attacker, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         Player player = (Player)(Object)this;
         if (!player.level().isClientSide()) {
            float f = attacker.getSecondsToDisableBlocking();

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && !(f <= 0.0F) && goal instanceof HaveShieldDisabledGoal) {
                  lockout.completeGoal(goal, player);
               }
            }

         }
      }
   }
}
