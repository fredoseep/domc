package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToEntityGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class DieToPolarBearGoal extends DieToEntityGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_polar_bear.png");

   public DieToPolarBearGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die to Polar Bear";
   }

   public EntityType getEntityType() {
      return EntityType.POLAR_BEAR;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
