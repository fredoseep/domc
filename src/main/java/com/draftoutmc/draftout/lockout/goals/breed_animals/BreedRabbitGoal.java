package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedAnimalGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class BreedRabbitGoal extends BreedAnimalGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/breed/breed_rabbit.png");

   public BreedRabbitGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Breed Rabbits";
   }

   public EntityType<?> getAnimal() {
      return EntityType.RABBIT;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
