package com.draftoutmc.draftout.match.standardize.barter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class BarterSavedData extends SavedData {
   public static final Codec<BarterSavedData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf("barterCount").forGetter((d) -> d.barterCount)).apply(instance, BarterSavedData::new));
   public static final SavedDataType<BarterSavedData> TYPE;
   private int barterCount;

   public BarterSavedData() {
      this.barterCount = 0;
   }

   public BarterSavedData(int barterCount) {
      this.barterCount = barterCount;
   }

   public static BarterSavedData get(ServerLevel level) {
      return (BarterSavedData)level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
   }

   public int getAndIncrement() {
      int current = this.barterCount++;
      this.setDirty();
      return current;
   }

   static {
      TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("draftout", "barter_count"), BarterSavedData::new, CODEC, DataFixTypes.LEVEL);
   }
}
