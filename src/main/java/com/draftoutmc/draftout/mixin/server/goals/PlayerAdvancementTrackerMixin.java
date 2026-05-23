package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import com.draftoutmc.draftout.lockout.interfaces.GetUniqueAdvancementsGoal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.interfaces.VisitBiomeGoal;
import com.draftoutmc.draftout.lockout.interfaces.VisitBiomesGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.LinkedHashSet;
import java.util.Optional;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerAdvancements.class})
public abstract class PlayerAdvancementTrackerMixin {
   @Shadow
   private ServerPlayer player;
   @Unique
   private static final Identifier ADVENTURING_TIME = Identifier.fromNamespaceAndPath("minecraft", "adventure/adventuring_time");
   @Unique
   private static final Identifier HOT_TOURIST_DESTINATIONS = Identifier.fromNamespaceAndPath("minecraft", "nether/explore_nether");

   @Redirect(
      method = {"lambda$award$0"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
)
   )
   public void onBroadcastInChat(PlayerList instance, Component message, boolean overlay) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (!Lockout.isLockoutRunning(lockout) || lockout.isLockoutPlayer(this.player.getUUID())) {
         instance.broadcastSystemMessage(message, overlay);
      }

   }

   @Inject(
      method = {"award"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/advancements/Advancement;rewards()Lnet/minecraft/advancements/AdvancementRewards;"
)}
   )
   public void onGrantCriterion(AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (lockout.isLockoutPlayer(this.player.getUUID())) {
            LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(this.player.getUUID());
            holder.value().display().ifPresent((display) -> {
               if (LockoutMatchData.isInMatch() && display.shouldAnnounceChat()) {
                  ServerConnection.getInstance().sendBroadcastAdvancement(holder.id().toString());
               }

            });

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted()) {
                  if (goal instanceof AdvancementGoal) {
                     AdvancementGoal advancementGoal = (AdvancementGoal)goal;
                     if (advancementGoal.getAdvancements().contains(holder.id())) {
                        lockout.completeGoal(goal, (Player)this.player);
                     }
                  }

                  if (goal instanceof GetUniqueAdvancementsGoal) {
                     GetUniqueAdvancementsGoal getUniqueAdvancementsGoal = (GetUniqueAdvancementsGoal)goal;
                     Optional<DisplayInfo> advancementDisplay = holder.value().display();
                     if (advancementDisplay.isPresent()) {
                        getUniqueAdvancementsGoal.getTrackerMap().putIfAbsent(team, new LinkedHashSet());
                        ((LinkedHashSet)getUniqueAdvancementsGoal.getTrackerMap().get(team)).add(holder.id());
                        int size = ((LinkedHashSet)getUniqueAdvancementsGoal.getTrackerMap().get(team)).size();
                        team.sendTooltipUpdate(getUniqueAdvancementsGoal);
                        if (size >= getUniqueAdvancementsGoal.getAmount()) {
                           lockout.completeGoal(goal, (LockoutTeam)team);
                        }
                     }
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"award"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/advancements/AdvancementProgress;isDone()Z",
   ordinal = 1,
   shift = Shift.BEFORE
)}
   )
   public void onAdvancementProgress(AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (holder.id().equals(ADVENTURING_TIME) || holder.id().equals(HOT_TOURIST_DESTINATIONS)) {
            Identifier biomeId = Identifier.parse(criterion);
            if (lockout.isLockoutPlayer(this.player.getUUID())) {
               LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(this.player.getUUID());

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted()) {
                     if (goal instanceof VisitBiomeGoal) {
                        VisitBiomeGoal visitBiomeGoal = (VisitBiomeGoal)goal;
                        if (visitBiomeGoal.getBiomes().contains(biomeId)) {
                           lockout.completeGoal(goal, (Player)this.player);
                        }
                     }

                     if (goal instanceof VisitBiomesGoal) {
                        VisitBiomesGoal visitBiomesGoal = (VisitBiomesGoal)goal;
                        if (visitBiomesGoal.getBiomes().contains(biomeId)) {
                           visitBiomesGoal.getTrackerMap().putIfAbsent(team, new LinkedHashSet());
                           ((LinkedHashSet)visitBiomesGoal.getTrackerMap().get(team)).add(biomeId);
                           int size = ((LinkedHashSet)visitBiomesGoal.getTrackerMap().get(team)).size();
                           team.sendTooltipUpdate((Goal&HasTooltipInfo)goal);
                           if (size >= visitBiomesGoal.getBiomes().size()) {
                              lockout.completeGoal(visitBiomesGoal, (LockoutTeam)team);
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }
}
