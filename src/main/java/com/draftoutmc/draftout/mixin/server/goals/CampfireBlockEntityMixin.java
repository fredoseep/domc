package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.FillCampfireWithFoodGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CampfireBlockEntity.class})
public class CampfireBlockEntityMixin {
   @Inject(
      method = {"placeFood"},
      at = {@At("RETURN")}
   )
   public void addItem(ServerLevel serverLevel, LivingEntity sourceEntity, ItemStack placeItem, CallbackInfoReturnable<Boolean> cir) {
      Lockout lockout = LockoutMatchData.getLockout();
      if (Lockout.isLockoutRunning(lockout)) {
         if (sourceEntity instanceof Player) {
            Player player = (Player)sourceEntity;
            if (cir.getReturnValueZ()) {
               CampfireBlockEntity campfire = (CampfireBlockEntity)(Object)this;
               boolean filled = true;

               for(ItemStack itemStack : campfire.getItems()) {
                  if (itemStack.isEmpty()) {
                     filled = false;
                     break;
                  }
               }

               if (!filled) {
                  return;
               }

               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof FillCampfireWithFoodGoal) {
                     lockout.completeGoal(goal, player);
                  }
               }

               return;
            }
         }

      }
   }
}
