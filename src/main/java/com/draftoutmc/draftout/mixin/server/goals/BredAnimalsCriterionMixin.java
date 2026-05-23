package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.BreedAnimalGoal;
import com.draftoutmc.draftout.lockout.interfaces.BreedUniqueAnimalsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.LinkedHashSet;
import net.minecraft.advancements.criterion.BredAnimalsTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BredAnimalsTrigger.class})
public class BredAnimalsCriterionMixin {
   @Inject(
      method = {"trigger"},
      at = {@At("HEAD")}
   )
   public void onBreedAnimal(ServerPlayer player, Animal parent, Animal partner, @Nullable AgeableMob child, CallbackInfo ci) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         for(Goal goal : lockout.getBoard().getGoals()) {
            if (goal != null && !goal.isCompleted()) {
               if (goal instanceof BreedAnimalGoal) {
                  BreedAnimalGoal breedAnimalGoal = (BreedAnimalGoal)goal;
                  if (parent.getType().equals(breedAnimalGoal.getAnimal())) {
                     lockout.completeGoal(breedAnimalGoal, (Player)player);
                  }
               }

               if (goal instanceof BreedUniqueAnimalsGoal) {
                  BreedUniqueAnimalsGoal breedUniqueAnimalsGoal = (BreedUniqueAnimalsGoal)goal;
                  LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());
                  lockout.bredAnimalTypes.computeIfAbsent(team, (t) -> new LinkedHashSet());
                  ((LinkedHashSet)lockout.bredAnimalTypes.get(team)).add(parent.getType());
                  int size = ((LinkedHashSet)lockout.bredAnimalTypes.get(team)).size();
                  team.sendTooltipUpdate(breedUniqueAnimalsGoal);
                  if (size >= breedUniqueAnimalsGoal.getAmount()) {
                     lockout.completeGoal(breedUniqueAnimalsGoal, (LockoutTeam)team);
                  }
               }
            }
         }

      }
   }
}
