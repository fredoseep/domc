package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.misc.FillArmorStandGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.ArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ArmorStand.class})
public class ArmorStandMixin {
   @Inject(
      method = {"interact"},
      at = {@At("RETURN")}
   )
   public void onInteractAt(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
      if (!player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            ArmorStand armorStand = (ArmorStand)(Object)this;

            for(Goal goal : lockout.getBoard().getGoals()) {
               if (goal != null && goal instanceof FillArmorStandGoal) {
                  FillArmorStandGoal fillArmorStandGoal = (FillArmorStandGoal)goal;
                  if (!goal.isCompleted()) {
                     ArrayList<ItemStack> armor = new ArrayList();
                     armor.add(armorStand.getItemBySlot(EquipmentSlot.HEAD));
                     armor.add(armorStand.getItemBySlot(EquipmentSlot.CHEST));
                     armor.add(armorStand.getItemBySlot(EquipmentSlot.LEGS));
                     armor.add(armorStand.getItemBySlot(EquipmentSlot.FEET));
                     if (serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && cir.getReturnValue() == InteractionResult.SUCCESS_SERVER) {
                        for(ItemStack armorItem : armor) {
                           if (armorItem == null || armorItem.isEmpty()) {
                              return;
                           }
                        }

                        lockout.completeGoal(fillArmorStandGoal, (Player)player);
                        return;
                     }
                  }
               }
            }

         }
      }
   }
}
