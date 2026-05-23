package com.draftoutmc.draftout.match.standardize.villager_trades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class VillagerTradeSavedData extends SavedData {
   public static final Codec<VillagerTradeSavedData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("counters").forGetter((d) -> d.counters)).apply(instance, VillagerTradeSavedData::new));
   public static final SavedDataType<VillagerTradeSavedData> TYPE;
   private final Map<String, Integer> counters;

   public VillagerTradeSavedData() {
      this.counters = new HashMap();
   }

   public VillagerTradeSavedData(Map<String, Integer> counters) {
      this.counters = new HashMap(counters);
   }

   public static VillagerTradeSavedData get(ServerLevel level) {
      return (VillagerTradeSavedData)level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
   }

   public int getAndIncrement(String professionId, int tradeLevel) {
      String key = professionId + ":" + tradeLevel;
      int current = (Integer)this.counters.getOrDefault(key, 0);
      this.counters.put(key, current + 1);
      this.setDirty();
      return current;
   }

   static {
      TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("draftout", "villager_trade_counters"), VillagerTradeSavedData::new, CODEC, DataFixTypes.LEVEL);
   }
}
