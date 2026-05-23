package com.draftoutmc.draftout.match.standardize.villager_trades;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.TradeSets;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class VillagerTradeGuarantee {
   public static void guaranteeTrades(Villager villager, ServerLevel level, ResourceKey<TradeSet> tradeSetKey) {
      if (tradeSetKey.equals(TradeSets.ARMORER_LEVEL_3)) {
         guaranteeChainmailArmor(villager, level);
      }

      if (tradeSetKey.equals(TradeSets.CARTOGRAPHER_LEVEL_3)) {
         guaranteeTrailChambersMap(villager, level);
      }

   }

   private static void guaranteeChainmailArmor(Villager villager, ServerLevel level) {
      if (!sellsChainArmor(villager.getOffers())) {
         int replaceIdx = 4 + villager.random.nextInt(2);
         ResourceKey var10000;
         switch (villager.random.nextInt(2)) {
            case 0 -> var10000 = VillagerTrades.ARMORER_3_EMERALD_CHAINMAIL_CHESTPLATE;
            case 1 -> var10000 = VillagerTrades.ARMORER_3_EMERALD_CHAINMAIL_HELMET;
            default -> throw new IllegalStateException();
         }

         ResourceKey<VillagerTrade> chosenKey = var10000;
         VillagerTrade trade = (VillagerTrade)level.registryAccess().lookupOrThrow(Registries.VILLAGER_TRADE).getValue(chosenKey);
         MerchantOffer newOffer = trade.getOffer((new LootContext.Builder((new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, villager.position()).withParameter(LootContextParams.THIS_ENTITY, villager).withParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, Unit.INSTANCE).create(LootContextParamSets.VILLAGER_TRADE))).create(Optional.empty()));
         villager.getOffers().set(replaceIdx, newOffer);
      }
   }

   private static void guaranteeTrailChambersMap(Villager villager, ServerLevel level) {
      List<MerchantOffer> offers = villager.getOffers();
      boolean hasMap = offers.stream().anyMatch((o) -> isTrialChambersMap(o.getResult()));
      if (!hasMap) {
         VillagerTrade trade = (VillagerTrade)level.registryAccess().lookupOrThrow(Registries.VILLAGER_TRADE).getValue(VillagerTrades.CARTOGRAPHER_3_EMERALD_AND_COMPASS_TRIAL_CHAMBER_MAP);
         MerchantOffer newOffer = trade.getOffer((new LootContext.Builder((new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, villager.position()).withParameter(LootContextParams.THIS_ENTITY, villager).withParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED, Unit.INSTANCE).create(LootContextParamSets.VILLAGER_TRADE))).create(Optional.empty()));
         if (newOffer != null) {
            int replaceIdx = villager.random.nextInt(2);
            offers.set(offers.size() - 1 - replaceIdx, newOffer);
         }
      }
   }

   private static boolean isTrialChambersMap(ItemStack item) {
      if (!item.is(Items.FILLED_MAP)) {
         return false;
      } else {
         Component name = (Component)item.get(DataComponents.ITEM_NAME);
         if (name == null) {
            return false;
         } else {
            ComponentContents var3 = name.getContents();
            boolean var10000;
            if (var3 instanceof TranslatableContents) {
               TranslatableContents tc = (TranslatableContents)var3;
               if (tc.getKey().equals("filled_map.trial_chambers")) {
                  var10000 = true;
                  return var10000;
               }
            }

            var10000 = false;
            return var10000;
         }
      }
   }

   private static boolean sellsChainArmor(List<MerchantOffer> offers) {
      for(MerchantOffer offer : offers) {
         Item item = offer.getResult().getItem();
         if (item.equals(Items.CHAINMAIL_BOOTS) || item.equals(Items.CHAINMAIL_CHESTPLATE) || item.equals(Items.CHAINMAIL_HELMET) || item.equals(Items.CHAINMAIL_LEGGINGS)) {
            return true;
         }
      }

      return false;
   }
}
