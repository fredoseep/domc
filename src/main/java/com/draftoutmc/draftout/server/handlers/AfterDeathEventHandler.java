package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.death.DieToFallingOffVinesGoal;
import com.draftoutmc.draftout.lockout.goals.death.DieToTNTMinecartGoal;
import com.draftoutmc.draftout.lockout.goals.kill.Kill100MobsGoal;
import com.draftoutmc.draftout.lockout.goals.kill.KillBreezeWithWindChargeGoal;
import com.draftoutmc.draftout.lockout.goals.kill.KillColoredSheepGoal;
import com.draftoutmc.draftout.lockout.goals.kill.KillOtherTeamPlayer;
import com.draftoutmc.draftout.lockout.goals.kill.KillSnowGolemInNetherGoal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentDies3TimesGoal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentDiesGoal;
import com.draftoutmc.draftout.lockout.interfaces.DieToDamageTypeGoal;
import com.draftoutmc.draftout.lockout.interfaces.DieToEntityGoal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.interfaces.KillAllSpecificMobsGoal;
import com.draftoutmc.draftout.lockout.interfaces.KillMobGoal;
import com.draftoutmc.draftout.lockout.interfaces.KillSpecificMobsGoal;
import com.draftoutmc.draftout.lockout.interfaces.KillUniqueHostileMobsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.FallLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AfterDeathEventHandler implements ServerLivingEntityEvents.AfterDeath {
    public void afterDeath(LivingEntity entity, DamageSource source) {
        Lockout lockout = LockoutMatchData.getLockout();
        if (Lockout.isLockoutRunning(lockout)) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (!lockout.isLockoutPlayer(player)) {
                    return;
                }
            }

            boolean playerDied = entity instanceof Player;
            boolean mobDied = !playerDied;
            boolean killedByPlayer = entity.getKillCredit() instanceof Player;
            if (playerDied) {
                LockoutTeam team = lockout.getPlayerTeam(entity.getUUID());
                lockout.deaths.putIfAbsent(team, 0);
                lockout.deaths.merge(team, 1, Integer::sum);
            }

            if (mobDied && killedByPlayer) {
                Player killer = (Player) entity.getKillCredit();
                if (lockout.isLockoutPlayer(killer.getUUID())) {
                    LockoutTeam team = lockout.getPlayerTeam(killer.getUUID());
                    lockout.mobsKilled.putIfAbsent(team, 0);
                    lockout.mobsKilled.merge(team, 1, Integer::sum);
                }
            }

            for (Goal goal : lockout.getBoard().getGoals()) {
                if (goal != null && !goal.isCompleted()) {
                    if (mobDied && killedByPlayer) {
                        Player killer = (Player) entity.getKillCredit();
                        if (goal instanceof KillMobGoal) {
                            KillMobGoal killMobGoal = (KillMobGoal) goal;
                            if (killMobGoal.getEntity().equals(entity.getType())) {
                                boolean allow = true;
                                if (goal instanceof KillSnowGolemInNetherGoal) {
                                    allow = killer.level().dimension() == ServerLevel.NETHER;
                                }

                                if (goal instanceof KillBreezeWithWindChargeGoal) {
                                    allow = source.is(DamageTypes.WIND_CHARGE);
                                }

                                if (goal instanceof KillColoredSheepGoal) {
                                    KillColoredSheepGoal killColoredSheepGoal = (KillColoredSheepGoal) goal;
                                    allow = ((Sheep) entity).getColor() == killColoredSheepGoal.getDyeColor();
                                }

                                if (allow) {
                                    lockout.completeGoal(goal, killer);
                                }
                            }
                        }

                        LockoutTeamServer team = (LockoutTeamServer) lockout.getPlayerTeam(killer.getUUID());
                        if (goal instanceof KillAllSpecificMobsGoal) {
                            KillAllSpecificMobsGoal killAllSpecificMobsGoal = (KillAllSpecificMobsGoal) goal;
                            if (killAllSpecificMobsGoal.getEntityTypes().contains(entity.getType())) {
                                killAllSpecificMobsGoal.getTrackerMap().computeIfAbsent(team, (t) -> new LinkedHashSet());
                                ((LinkedHashSet) killAllSpecificMobsGoal.getTrackerMap().get(team)).add(entity.getType());
                                int size = ((LinkedHashSet) killAllSpecificMobsGoal.getTrackerMap().get(team)).size();
                                team.sendTooltipUpdate((Goal & HasTooltipInfo) goal);
                                if (size >= killAllSpecificMobsGoal.getEntityTypes().size()) {
                                    lockout.completeGoal(killAllSpecificMobsGoal, (LockoutTeam) team);
                                }
                            }
                        }

                        if (goal instanceof KillUniqueHostileMobsGoal) {
                            KillUniqueHostileMobsGoal killUniqueHostileMobsGoal = (KillUniqueHostileMobsGoal) goal;
                            if (entity instanceof Enemy || isHostileHorse(entity)) {
                                lockout.killedHostileTypes.computeIfAbsent(team, (t) -> new LinkedHashSet());
                                ((LinkedHashSet) lockout.killedHostileTypes.get(team)).add(entity.getType());
                                int size = ((LinkedHashSet) lockout.killedHostileTypes.get(team)).size();
                                team.sendTooltipUpdate((Goal & HasTooltipInfo) goal);
                                if (size >= killUniqueHostileMobsGoal.getAmount()) {
                                    lockout.completeGoal(killUniqueHostileMobsGoal, (LockoutTeam) team);
                                }
                            }
                        }

                        if (goal instanceof Kill100MobsGoal) {
                            Kill100MobsGoal kill100MobsGoal = (Kill100MobsGoal) goal;
                            int size = (Integer) lockout.mobsKilled.get(team);
                            team.sendTooltipUpdate((Goal & HasTooltipInfo) goal);
                            if (size >= kill100MobsGoal.getAmount()) {
                                lockout.completeGoal(goal, (LockoutTeam) team);
                            }
                        }

                        if (goal instanceof KillSpecificMobsGoal) {
                            KillSpecificMobsGoal killSpecificMobsGoal = (KillSpecificMobsGoal) goal;
                            if (killSpecificMobsGoal.getEntityTypes().contains(entity.getType())) {
                                killSpecificMobsGoal.getTrackerMap().putIfAbsent(team, 0);
                                killSpecificMobsGoal.getTrackerMap().merge(team, 1, Integer::sum);
                                int size = (Integer) killSpecificMobsGoal.getTrackerMap().get(team);
                                team.sendTooltipUpdate((Goal & HasTooltipInfo) goal);
                                if (size >= killSpecificMobsGoal.getAmount()) {
                                    lockout.completeGoal(killSpecificMobsGoal, (Player) killer);
                                }
                            }
                        }
                    }

                    if (playerDied) {
                        Player player = (Player) entity;
                        LockoutTeam team = lockout.getPlayerTeam(player.getUUID());
                        if (goal instanceof OpponentDiesGoal) {
                            lockout.complete1v1Goal(goal, player, false, player.getName().getString() + " died.");
                        }

                        if (goal instanceof OpponentDies3TimesGoal && (Integer) lockout.deaths.get(team) >= 3) {
                            lockout.complete1v1Goal(goal, player, false, team.getDisplayName() + " died 3 times.");
                        }

                        if (goal instanceof DieToDamageTypeGoal) {
                            DieToDamageTypeGoal dieToDamageTypeGoal = (DieToDamageTypeGoal) goal;

                            for (ResourceKey<DamageType> key : dieToDamageTypeGoal.getDamageRegistryKeys()) {
                                if (source.typeHolder().is(key)) {
                                    lockout.completeGoal(goal, player);
                                }
                            }
                        }

                        if (goal instanceof DieToEntityGoal) {
                            DieToEntityGoal dieToEntityGoal = (DieToEntityGoal) goal;
                            if (source.getEntity() != null && source.getEntity().getType() == dieToEntityGoal.getEntityType()) {
                                lockout.completeGoal(goal, player);
                            }
                        }

                        if (goal instanceof DieToFallingOffVinesGoal && source.typeHolder().is(DamageTypes.FALL)) {
                            FallLocation fallLocation = FallLocation.getCurrentFallLocation(player);
                            if (fallLocation != null) {
                                boolean isOther = FallLocation.OTHER_CLIMBABLE.id().equals(fallLocation.id());
                                boolean isLushCaveVines = false;
                                if (isOther) {
                                    Optional<BlockPos> lastClimbablePos = player.getLastClimbablePos();
                                    BlockState blockState = player.level().getBlockState((BlockPos) lastClimbablePos.get());
                                    isLushCaveVines = blockState.is(Blocks.CAVE_VINES) || blockState.is(Blocks.CAVE_VINES_PLANT);
                                }

                                if (isLushCaveVines || List.of(FallLocation.VINES, FallLocation.TWISTING_VINES, FallLocation.WEEPING_VINES).contains(fallLocation)) {
                                    lockout.completeGoal(goal, player);
                                }
                            }
                        }

                        if (goal instanceof DieToTNTMinecartGoal && source.getDirectEntity() instanceof MinecartTNT) {
                            lockout.completeGoal(goal, player);
                        }

                        if (goal instanceof KillOtherTeamPlayer && killedByPlayer) {
                            Player killer = (Player) entity.getKillCredit();
                            if (!Objects.equals(player, killer) && !Objects.equals(lockout.getPlayerTeam(killer.getUUID()), lockout.getPlayerTeam(player.getUUID()))) {
                                lockout.completeGoal(goal, killer);
                            }
                        }
                    }
                }
            }

        }
    }

    private static boolean isHostileHorse(Entity entity) {
        if (entity == null) {
            return false;
        } else if (!(entity instanceof SkeletonHorse) && !(entity instanceof ZombieHorse)) {
            return false;
        } else {
            for (Entity passenger : entity.getPassengers()) {
                if (passenger instanceof Enemy) {
                    return true;
                }
            }

            return false;
        }
    }
}
