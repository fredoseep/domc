package com.draftoutmc.draftout.mixin.server.goals;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.CraftUniqueItemsGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ResultSlot.class})
public class CraftingResultSlotMixin {
   @Shadow
   @Final
   private Player player;
   @Shadow
   private int removeCount;

   @Inject(
      method = {"checkTakeAchievements"},
      at = {@At("HEAD")}
   )
   public void onCraft(ItemStack carried, CallbackInfo ci) {
      if (!this.player.level().isClientSide()) {
         Lockout lockout = LockoutMatchData.getLockout();
         if (Lockout.isLockoutRunning(lockout)) {
            if (this.removeCount >= 0 && !carried.isEmpty()) {
               if (this.player.containerMenu instanceof CraftingMenu || this.player.containerMenu instanceof InventoryMenu) {
                  if (lockout.isLockoutPlayer(this.player.getUUID())) {
                     LockoutTeamServer team = (LockoutTeamServer)lockout.getPlayerTeam(this.player.getUUID());
                     lockout.uniqueCrafts.putIfAbsent(this.player.getUUID(), new HashSet());
                     Set<Item> crafts = (Set)lockout.uniqueCrafts.get(this.player.getUUID());
                     boolean addedNew = crafts.add(carried.getItem());
                     if (addedNew) {
                        boolean shouldDisplay = false;

                        for(Goal goal : lockout.getBoard().getGoals()) {
                           if (goal != null && !goal.isMarkedAsCompleted() && goal instanceof CraftUniqueItemsGoal) {
                              CraftUniqueItemsGoal craftUniqueItemsGoal = (CraftUniqueItemsGoal)goal;
                              shouldDisplay = true;
                              team.sendTooltipUpdate(craftUniqueItemsGoal, this.player);
                              if (crafts.size() >= craftUniqueItemsGoal.getAmount()) {
                                 lockout.completeGoal(goal, this.player);
                              }
                           }
                        }

                        if (shouldDisplay) {
                           this.player.level().playSound((Entity)null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, SoundSource.MASTER, 2.0F, 2.0F);
                           if (crafts.size() % 5 == 0) {
                              Player var10000 = this.player;
                              String var10001 = String.valueOf(ChatFormatting.GRAY);
                              var10000.sendSystemMessage(Component.nullToEmpty(var10001 + String.valueOf(ChatFormatting.ITALIC) + "You have crafted " + crafts.size() + " unique items."));
                           }

                           this.player.sendOverlayMessage(Component.nullToEmpty("Unique crafts: " + crafts.size()));
                        }

                     }
                  }
               }
            }
         }
      }
   }
}
