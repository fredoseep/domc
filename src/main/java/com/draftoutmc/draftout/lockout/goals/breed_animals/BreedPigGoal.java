package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedAnimalGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class BreedPigGoal extends BreedAnimalGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/breed/breed_pig.png");

   public BreedPigGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Breed Pigs";
   }

   public EntityType<?> getAnimal() {
      return EntityType.PIG;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
