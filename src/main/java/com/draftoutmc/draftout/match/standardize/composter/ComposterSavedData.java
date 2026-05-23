package com.draftoutmc.draftout.match.standardize.composter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class ComposterSavedData extends SavedData {
   public static final Codec<ComposterSavedData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf("counter").forGetter((d) -> d.counter)).apply(instance, ComposterSavedData::new));
   public static final SavedDataType<ComposterSavedData> TYPE;
   private int counter;

   public ComposterSavedData() {
      this.counter = 0;
   }

   public ComposterSavedData(int counter) {
      this.counter = counter;
   }

   public static ComposterSavedData get(ServerLevel level) {
      return (ComposterSavedData)level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
   }

   public int getAndIncrement() {
      int current = this.counter++;
      this.setDirty();
      return current;
   }

   static {
      TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("draftout", "composter_counter"), ComposterSavedData::new, CODEC, DataFixTypes.LEVEL);
   }
}
