package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.standardize.villager_trades.VillagerTradeGuarantee;
import com.draftoutmc.draftout.match.standardize.villager_trades.VillagerTradeSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Villager.class})
public abstract class VillagerMixin {
   @Unique
   private int lockout$lastGeneratedLevel = 0;

   @Inject(
      method = {"updateTrades"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void lockout$cancelUpdateTrades(ServerLevel level, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"setVillagerData"},
      at = {@At("HEAD")}
   )
   private void lockout$onSetVillagerData(VillagerData newData, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         Villager villager = (Villager)(Object)this;
         if (!villager.getVillagerData().profession().equals(newData.profession())) {
            this.lockout$lastGeneratedLevel = 0;
         }

      }
   }

   @Inject(
      method = {"mobInteract"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/npc/villager/Villager;getOffers()Lnet/minecraft/world/item/trading/MerchantOffers;",
   ordinal = 0
)}
   )
   private void lockout$onBeforeOffersCheck(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
      if (LockoutMatchData.isInMatch()) {
         Villager villager = (Villager)(Object)this;
         Level var6 = villager.level();
         if (var6 instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)var6;
            this.lockout$generateIfNeeded(serverLevel);
         }

      }
   }

   @Inject(
      method = {"addAdditionalSaveData"},
      at = {@At("TAIL")}
   )
   private void lockout$onSave(ValueOutput output, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         output.putInt("StdTradesGeneratedLevel", this.lockout$lastGeneratedLevel);
      }
   }

   @Inject(
      method = {"readAdditionalSaveData"},
      at = {@At("TAIL")}
   )
   private void lockout$onLoad(ValueInput input, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         this.lockout$lastGeneratedLevel = input.getIntOr("StdTradesGeneratedLevel", 0);
      }
   }

   @Unique
   private void lockout$generateIfNeeded(ServerLevel level) {
      Villager villager = (Villager)(Object)this;
      VillagerData data = villager.getVillagerData();
      if (!data.profession().is(VillagerProfession.NONE) && !data.profession().is(VillagerProfession.NITWIT)) {
         int currentLevel = data.level();

         while(this.lockout$lastGeneratedLevel < currentLevel) {
            ++this.lockout$lastGeneratedLevel;
            this.lockout$generateForLevel(level, data, this.lockout$lastGeneratedLevel);
         }

      }
   }

   @Unique
   private void lockout$generateForLevel(ServerLevel level, VillagerData data, int tradeLevel) {
      Villager villager = (Villager)(Object)this;
      VillagerProfession profession = (VillagerProfession)data.profession().value();
      ResourceKey<TradeSet> tradeSetKey = profession.getTrades(tradeLevel);
      if (tradeSetKey != null) {
         String professionId = (String)data.profession().unwrapKey().map((k) -> k.identifier().toString()).orElse("unknown");
         int counter = VillagerTradeSavedData.get(level).getAndIncrement(professionId, tradeLevel);
         long seed = level.getSeed() ^ (long)professionId.hashCode() * -7046029254386353131L ^ (long)tradeLevel * 7809847782465536322L ^ (long)counter * -2960836687051489901L;
         RandomSource originalRandom = villager.random;
         villager.random = RandomSource.create(seed);

         try {
            villager.addOffersFromTradeSet(level, villager.getOffers(), tradeSetKey);
            VillagerTradeGuarantee.guaranteeTrades(villager, level, tradeSetKey);
         } finally {
            villager.random = originalRandom;
         }

      }
   }
}
