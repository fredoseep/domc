package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.workstation.UseComposterGoal;
import com.draftoutmc.draftout.lockout.interfaces.CompostUniqueFoodsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.standardize.composter.ComposterHelper;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.LinkedHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ComposterBlock.class})
public class ComposterBlockMixin {
   @Inject(
      method = {"extractProduce"},
      at = {@At("RETURN")}
   )
   private static void emptyFullComposterMixin(Entity sourceEntity, BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            if (sourceEntity instanceof Player) {
               Player player = (Player)sourceEntity;

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof UseComposterGoal) {
                     lockout.completeGoal(goal, player);
                  }
               }

            }
         }
      }
   }

   @Redirect(
      method = {"addItem"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/RandomSource;nextDouble()D"
)
   )
   private static double onComposterRng(RandomSource instance, @Local(name = {"level"},argsOnly = true) LevelAccessor level) {
      if (level instanceof ServerLevel serverLevel) {
         if (LockoutMatchData.isInMatch()) {
            return ComposterHelper.attempt(serverLevel);
         }
      }

      return instance.nextDouble();
   }

   @Inject(
      method = {"addItem"},
      at = {@At("HEAD")}
   )
   private static void onAddItem(Entity sourceEntity, BlockState state, LevelAccessor level, BlockPos pos, ItemStack itemStack, CallbackInfoReturnable<BlockState> cir) {
      if (!level.isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            if (sourceEntity instanceof Player) {
               Player player = (Player)sourceEntity;
               if (lockout.isLockoutPlayer(player.getUUID())) {
                  LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());

                  for(Goal goal : lockout.getBoard().getGoals()) {
                     if (goal != null && !goal.isCompleted() && goal instanceof CompostUniqueFoodsGoal) {
                        CompostUniqueFoodsGoal compostUniqueFoodsGoal = (CompostUniqueFoodsGoal)goal;
                        FoodProperties foodComponent = (FoodProperties)itemStack.get(DataComponents.FOOD);
                        if (foodComponent != null) {
                           compostUniqueFoodsGoal.getTrackerMap().putIfAbsent(team, new LinkedHashSet());
                           ((LinkedHashSet)compostUniqueFoodsGoal.getTrackerMap().get(team)).add(itemStack.getItem());
                           int size = ((LinkedHashSet)compostUniqueFoodsGoal.getTrackerMap().get(team)).size();
                           team.sendTooltipUpdate(compostUniqueFoodsGoal, player);
                           if (size >= compostUniqueFoodsGoal.getAmount()) {
                              lockout.completeGoal(goal, (LockoutTeam)team);
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
