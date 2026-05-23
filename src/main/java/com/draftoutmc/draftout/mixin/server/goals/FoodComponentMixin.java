package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.opponent.OpponentEatsFoodGoal;
import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemGoal;
import com.draftoutmc.draftout.lockout.interfaces.ConsumeItemsGoal;
import com.draftoutmc.draftout.lockout.interfaces.EatUniqueFoodsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FoodProperties.class})
public class FoodComponentMixin {
   @Inject(
      method = {"onConsume"},
      at = {@At("HEAD")}
   )
   public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci) {
      if (!level.isClientSide()) {
         if (user instanceof Player) {
            Player player = (Player)user;
            Lockout lockout = LockoutMatchData.getLockout();
            if (Lockout.isLockoutRunning(lockout)) {
               if (lockout.isLockoutPlayer(player.getUUID())) {
                  LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(player.getUUID());

                  for(Goal goal : lockout.getBoard().getGoals()) {
                     if (goal != null && !goal.isCompleted()) {
                        if (goal instanceof ConsumeItemGoal) {
                           ConsumeItemGoal consumeItemGoal = (ConsumeItemGoal)goal;
                           if (consumeItemGoal.getItem().equals(stack.getItem())) {
                              lockout.completeGoal(goal, player);
                           }
                        }

                        if (goal instanceof EatUniqueFoodsGoal) {
                           EatUniqueFoodsGoal eatUniqueFoodsGoal = (EatUniqueFoodsGoal)goal;
                           FoodProperties foodComponent = (FoodProperties)stack.get(DataComponents.FOOD);
                           if (foodComponent != null) {
                              eatUniqueFoodsGoal.getTrackerMap().putIfAbsent(team, new LinkedHashSet());
                              ((LinkedHashSet)eatUniqueFoodsGoal.getTrackerMap().get(team)).add(stack.getItem());
                              int size = ((LinkedHashSet)eatUniqueFoodsGoal.getTrackerMap().get(team)).size();
                              team.sendTooltipUpdate(eatUniqueFoodsGoal);
                              if (size >= eatUniqueFoodsGoal.getAmount()) {
                                 lockout.completeGoal(goal, (LockoutTeam)team);
                              }
                           }
                        }

                        if (goal instanceof ConsumeItemsGoal) {
                           ConsumeItemsGoal consumeItemsGoal = (ConsumeItemsGoal)goal;
                           FoodProperties foodComponent = (FoodProperties)stack.get(DataComponents.FOOD);
                           if (foodComponent != null) {
                              consumeItemsGoal.getTrackerMap().putIfAbsent(team, new LinkedHashSet());
                              ((LinkedHashSet)consumeItemsGoal.getTrackerMap().get(team)).add(stack.getItem());
                              Set<Item> unconsumed = new HashSet(consumeItemsGoal.getItems());
                              unconsumed.removeAll((Collection)consumeItemsGoal.getTrackerMap().get(team));
                              team.sendTooltipUpdate(consumeItemsGoal);
                              if (unconsumed.isEmpty()) {
                                 lockout.completeGoal(goal, (LockoutTeam)team);
                              }
                           }
                        }

                        if (goal instanceof OpponentEatsFoodGoal) {
                           lockout.complete1v1Goal(goal, player, false, player.getName().getString() + " ate food.");
                        }
                     }
                  }

               }
            }
         }
      }
   }
}
