package com.draftoutmc.draftout.match.standardize.taming;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class TamingSavedData extends SavedData {
   public static final Codec<TamingSavedData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("counters").forGetter((d) -> d.counters)).apply(instance, TamingSavedData::new));
   public static final SavedDataType<TamingSavedData> TYPE;
   private final Map<String, Integer> counters;

   public TamingSavedData() {
      this.counters = new HashMap();
   }

   public TamingSavedData(Map<String, Integer> counters) {
      this.counters = new HashMap(counters);
   }

   public static TamingSavedData get(ServerLevel level) {
      return (TamingSavedData)level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
   }

   public int getAndIncrement(String mobType) {
      int current = (Integer)this.counters.getOrDefault(mobType, 0);
      this.counters.put(mobType, current + 1);
      this.setDirty();
      return current;
   }

   static {
      TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("draftout", "taming_counters"), TamingSavedData::new, CODEC, DataFixTypes.LEVEL);
   }
}
