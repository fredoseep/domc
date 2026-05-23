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

public class VisitAllCaveBiomesGoal extends VisitBiomesGoal implements TextureProvider {
   private static final List<Identifier> BIOMES = List.of(Identifier.withDefaultNamespace("lush_caves"), Identifier.withDefaultNamespace("dripstone_caves"), Identifier.withDefaultNamespace("deep_dark"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/visit_cave_biomes.png");

   public VisitAllCaveBiomesGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getBiomes() {
      return BIOMES;
   }

   public String getGoalName() {
      return "Visit all Cave Biomes";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public Map<LockoutTeam, LinkedHashSet<Identifier>> getTrackerMap() {
      return LockoutMatchData.getLockout().visitedCaveBiomes;
   }
}
