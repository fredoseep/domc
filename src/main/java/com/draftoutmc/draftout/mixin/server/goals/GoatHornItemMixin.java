package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.TootGoatHornGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({InstrumentItem.class})
public class GoatHornItemMixin {
   @Inject(
      method = {"use"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/item/InstrumentItem;play(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/Instrument;)V",
   shift = Shift.AFTER
)}
   )
   public void onUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && !goal.isCompleted() && goal instanceof TootGoatHornGoal) {
                  lockout.completeGoal(goal, player);
               }
            }

         }
      }
   }
}
