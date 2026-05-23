package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.standardize.barter.BarterSavedData;
import com.draftoutmc.draftout.match.standardize.barter.BarterTracker;
import com.draftoutmc.draftout.server.LockoutServer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PiglinAi.class})
public class PiglinAiMixin {
   @ModifyExpressionValue(
      method = {"stopHoldingOffHandItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/piglin/Piglin;Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;getBarterResponseItems(Lnet/minecraft/world/entity/monster/piglin/Piglin;)Ljava/util/List;"
)}
   )
   private static List<ItemStack> redirectBarterItems(List<ItemStack> original, @Local(name = {"body"},argsOnly = true) Piglin body) {
      BarterSavedData data = BarterSavedData.get((ServerLevel)body.level());
      int tradeIndex = data.getAndIncrement();
      return (List)BarterTracker.getOverrideItems(tradeIndex, LockoutServer.server.overworld().getSeed()).orElse(original);
   }
}
