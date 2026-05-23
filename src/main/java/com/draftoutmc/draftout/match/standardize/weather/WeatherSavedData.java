package com.draftoutmc.draftout.match.standardize.weather;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class WeatherSavedData extends SavedData {
   public static final Codec<WeatherSavedData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf("thunder_duration_counter").forGetter((d) -> d.thunderDurationCounter), Codec.INT.fieldOf("thunder_delay_counter").forGetter((d) -> d.thunderDelayCounter), Codec.INT.fieldOf("rain_duration_counter").forGetter((d) -> d.rainDurationCounter), Codec.INT.fieldOf("rain_delay_counter").forGetter((d) -> d.rainDelayCounter)).apply(instance, WeatherSavedData::new));
   public static final SavedDataType<WeatherSavedData> TYPE;
   private int thunderDurationCounter;
   private int thunderDelayCounter;
   private int rainDurationCounter;
   private int rainDelayCounter;

   public WeatherSavedData() {
   }

   public WeatherSavedData(int thunderDurationCounter, int thunderDelayCounter, int rainDurationCounter, int rainDelayCounter) {
      this.thunderDurationCounter = thunderDurationCounter;
      this.thunderDelayCounter = thunderDelayCounter;
      this.rainDurationCounter = rainDurationCounter;
      this.rainDelayCounter = rainDelayCounter;
   }

   public static WeatherSavedData get(ServerLevel level) {
      return (WeatherSavedData)level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
   }

   public int getAndIncrementThunderDuration() {
      this.setDirty();
      return this.thunderDurationCounter++;
   }

   public int getAndIncrementThunderDelay() {
      this.setDirty();
      return this.thunderDelayCounter++;
   }

   public int getAndIncrementRainDuration() {
      this.setDirty();
      return this.rainDurationCounter++;
   }

   public int getAndIncrementRainDelay() {
      this.setDirty();
      return this.rainDelayCounter++;
   }

   static {
      TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("draftout", "weather_counters"), WeatherSavedData::new, CODEC, DataFixTypes.LEVEL);
   }
}
