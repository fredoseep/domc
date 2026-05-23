package com.draftoutmc.draftout.match.standardize;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class SpawnLavaLakesData extends SavedData {
   public static final Codec<SpawnLavaLakesData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.BOOL.fieldOf("done").forGetter((d) -> d.done), BlockPos.CODEC.listOf().fieldOf("candidates").forGetter((d) -> d.candidates), BlockPos.CODEC.listOf().fieldOf("placedPositions").forGetter((d) -> d.placedPositions), Codec.INT.listOf().fieldOf("succeededTargets").forGetter((d) -> new ArrayList(d.succeededTargets)), Codec.INT.listOf().fieldOf("attemptedCandidates").forGetter((d) -> new ArrayList(d.attemptedCandidates))).apply(instance, SpawnLavaLakesData::new));
   public static final SavedDataType<SpawnLavaLakesData> TYPE;
   public boolean done;
   public List<BlockPos> candidates;
   public List<BlockPos> placedPositions;
   public Set<Integer> succeededTargets;
   public Set<Integer> attemptedCandidates;

   public SpawnLavaLakesData() {
      this.done = false;
      this.candidates = new ArrayList();
      this.placedPositions = new ArrayList();
      this.succeededTargets = new HashSet();
      this.attemptedCandidates = new HashSet();
   }

   public SpawnLavaLakesData(boolean done, List<BlockPos> candidates, List<BlockPos> placedPositions, List<Integer> succeededTargets, List<Integer> attemptedCandidates) {
      this.done = done;
      this.candidates = new ArrayList(candidates);
      this.placedPositions = new ArrayList(placedPositions);
      this.succeededTargets = new HashSet(succeededTargets);
      this.attemptedCandidates = new HashSet(attemptedCandidates);
   }

   public static SpawnLavaLakesData get(WorldGenLevel level) {
      return (SpawnLavaLakesData)level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
   }

   static {
      TYPE = new SavedDataType(Identifier.fromNamespaceAndPath("draftout", "spawn_lava_lakes"), SpawnLavaLakesData::new, CODEC, DataFixTypes.LEVEL);
   }
}
