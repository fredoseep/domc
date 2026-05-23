package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToEntityGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class DieToIronGolemGoal extends DieToEntityGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_golem.png");

   public DieToIronGolemGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die to Iron Golem";
   }

   public EntityType getEntityType() {
      return EntityType.IRON_GOLEM;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
