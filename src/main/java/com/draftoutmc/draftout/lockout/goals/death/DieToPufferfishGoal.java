package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToEntityGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class DieToPufferfishGoal extends DieToEntityGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_pufferfish.png");

   public DieToPufferfishGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die to Pufferfish";
   }

   public EntityType getEntityType() {
      return EntityType.PUFFERFISH;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
