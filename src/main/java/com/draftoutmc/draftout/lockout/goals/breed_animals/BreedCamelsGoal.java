package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedAnimalGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class BreedCamelsGoal extends BreedAnimalGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/breed/breed_camels.png");

   public BreedCamelsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Breed Camels";
   }

   public EntityType<?> getAnimal() {
      return EntityType.CAMEL;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
