package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToDamageTypeGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public class DieToTridentGoal extends DieToDamageTypeGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_trident.png");

   public DieToTridentGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die to Trident";
   }

   public List<ResourceKey<DamageType>> getDamageRegistryKeys() {
      return List.of(DamageTypes.TRIDENT);
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
