package com.draftoutmc.draftout.lockout.goals.biome;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.lockout.interfaces.VisitBiomesGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class VisitAllNetherBiomes extends VisitBiomesGoal implements TextureProvider {
   private static final List<Identifier> BIOMES = List.of(Identifier.withDefaultNamespace("nether_wastes"), Identifier.withDefaultNamespace("crimson_forest"), Identifier.withDefaultNamespace("warped_forest"), Identifier.withDefaultNamespace("soul_sand_valley"), Identifier.withDefaultNamespace("basalt_deltas"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/visit_nether_biomes.png");

   public VisitAllNetherBiomes(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getBiomes() {
      return BIOMES;
   }

   public String getGoalName() {
      return "Visit All Nether Biomes";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public Map<LockoutTeam, LinkedHashSet<Identifier>> getTrackerMap() {
      return LockoutMatchData.getLockout().visitedNetherBiomes;
   }
}
