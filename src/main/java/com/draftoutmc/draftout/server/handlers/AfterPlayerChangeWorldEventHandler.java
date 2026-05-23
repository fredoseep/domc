package com.draftoutmc.draftout.server.handlers;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.EnterDimensionGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class AfterPlayerChangeWorldEventHandler implements ServerEntityLevelChangeEvents.AfterPlayerChange {
   public void afterChangeLevel(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal != null && goal instanceof EnterDimensionGoal) {
               EnterDimensionGoal enterDimensionGoal = (EnterDimensionGoal)goal;
               if (!goal.isCompleted() && destination.dimension() == enterDimensionGoal.getWorldRegistryKey()) {
                  lockout.completeGoal(goal, (Player)player);
               }
            }
         }

      }
   }
}
