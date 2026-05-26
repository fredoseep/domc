package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.EmptyHungerBarGoal;
import com.draftoutmc.draftout.lockout.goals.misc.ReachBedrockGoal;
import com.draftoutmc.draftout.lockout.goals.misc.ReachHeightLimitGoal;
import com.draftoutmc.draftout.lockout.goals.misc.ReachNetherRoofGoal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentTouchesWaterGoal;
import com.draftoutmc.draftout.lockout.interfaces.ObtainItemsGoal;
import com.draftoutmc.draftout.lockout.interfaces.OpponentObtainsItemGoal;
import com.draftoutmc.draftout.lockout.interfaces.RideEntityGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.Objects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

public class EndServerTickEventHandler implements ServerTickEvents.EndTick {
   public void onEndTick(@NonNull MinecraftServer server) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal != null && !goal.isCompleted()) {
               for(ServerPlayer player : server.getPlayerList().getPlayers()) {
                  if (goal instanceof ObtainItemsGoal) {
                     ObtainItemsGoal obtainItemsGoal = (ObtainItemsGoal)goal;
                     if (obtainItemsGoal.satisfiedBy(player.getInventory())) {
                        if (goal instanceof OpponentObtainsItemGoal) {
                           OpponentObtainsItemGoal opponentObtainsItemGoal = (OpponentObtainsItemGoal)goal;
                           lockout.complete1v1Goal(goal, (Player)player, false, opponentObtainsItemGoal.getMessage(player));
                        } else {
                           lockout.completeGoal(goal, (Player)player);
                        }
                     }
                  }

                  if (goal instanceof RideEntityGoal rideEntityGoal) {
                      if (player.isPassenger()) {
                        EntityType<?> vehicle = player.getVehicle().getType();
                        if (rideEntityGoal.getEntityType().equals(vehicle)) {
                           lockout.completeGoal(goal, player);
                        }
                     }
                  }

                  if (goal instanceof EmptyHungerBarGoal && player.getFoodData().getFoodLevel() == 0) {
                     lockout.completeGoal(goal, (Player)player);
                  }

                  if (goal instanceof ReachHeightLimitGoal && player.getY() >= (double)320.0F && player.level().dimension() == ServerLevel.OVERWORLD) {
                     lockout.completeGoal(goal, (Player)player);
                  }

                  if (goal instanceof ReachNetherRoofGoal && player.getY() >= (double)128.0F && player.level().dimension() == ServerLevel.NETHER) {
                     lockout.completeGoal(goal, (Player)player);
                  }

                  if (goal instanceof ReachBedrockGoal && player.getY() < (double)10.0F && Objects.equals(player.level().getBlockState(player.blockPosition().below()).getBlock(), Blocks.BEDROCK)) {
                     lockout.completeGoal(goal, (Player)player);
                  }

                  if (goal instanceof OpponentTouchesWaterGoal && Objects.equals(player.level().getBlockState(player.blockPosition()).getBlock(), Blocks.WATER)) {
                     lockout.complete1v1Goal(goal, (Player)player, false, player.getName().getString() + " touched water.");
                  }
               }
            }
         }

      }
   }
}
