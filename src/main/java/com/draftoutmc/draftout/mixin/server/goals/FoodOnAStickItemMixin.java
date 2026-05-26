package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.goals.ride.RidePigGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FoodOnAStickItem.class})
public class FoodOnAStickItemMixin {
    public FoodOnAStickItemMixin() {
    }

    @Inject(
            method = {"use"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndConvertOnBreak(ILnet/minecraft/world/level/ItemLike;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"
            )}
    )
    public void onUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide()) {
            Lockout lockout = LockoutMatchData.getLockout();
            if (Lockout.isLockoutRunning(lockout)) {
                ItemStack item = player.getItemInHand(hand);
                if (item.is(Items.CARROT_ON_A_STICK)) {
                    for(Goal goal : lockout.getBoard().getGoals()) {
                        if (goal != null && !goal.isCompleted() && goal instanceof RidePigGoal) {
                            lockout.completeGoal(goal, player);
                        }
                    }

                }
            }
        }
    }
}
