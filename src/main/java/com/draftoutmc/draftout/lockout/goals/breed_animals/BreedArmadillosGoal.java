package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedAnimalGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class BreedArmadillosGoal extends BreedAnimalGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/breed/breed_armadillos.png");

   public BreedArmadillosGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Breed Armadillos";
   }

   public EntityType<?> getAnimal() {
      return EntityType.ARMADILLO;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
