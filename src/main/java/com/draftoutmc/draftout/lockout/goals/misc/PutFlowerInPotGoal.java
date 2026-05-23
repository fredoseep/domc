package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.interfaces.IncrementStatGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class PutFlowerInPotGoal extends IncrementStatGoal implements TextureProvider {
   private static final List<Identifier> STATS;
   private static final Identifier TEXTURE;

   public PutFlowerInPotGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getStats() {
      return STATS;
   }

   public String getGoalName() {
      return "Pot any plant in Flower Pot";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   static {
      STATS = List.of(Stats.POT_FLOWER);
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/flower_in_a_pot.png");
   }
}
