package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.DrinkPotionGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PotionContents.class})
public class PotionContentsComponentMixin {
   @Inject(
      method = {"onConsume"},
      at = {@At("HEAD")}
   )
   public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci) {
      if (!level.isClientSide()) {
         if (user instanceof Player) {
            Player player = (Player)user;
            PotionContents potionContents = (PotionContents)(Object)this;
            Lockout lockout = LockoutMatchData.getLockout();
            if (Lockout.isLockoutRunning(lockout)) {
               for(Goal goal : lockout.getBoard().getGoals()) {
                  if (goal != null && !goal.isCompleted() && goal instanceof DrinkPotionGoal) {
                     DrinkPotionGoal drinkPotionGoal = (DrinkPotionGoal)goal;
                     if (potionContents.potion().isPresent() && drinkPotionGoal.getPotion().equals(potionContents.potion().get())) {
                        lockout.completeGoal(goal, player);
                     }
                  }
               }

            }
         }
      }
   }
}
