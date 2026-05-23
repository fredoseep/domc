package com.draftoutmc.draftout.lockout.goals.spyglass;

import com.draftoutmc.draftout.lockout.interfaces.SpyOnMobGoal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class SpyOnIronGolemGoal extends SpyOnMobGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/spy/spy_iron_golem.png");

   public SpyOnIronGolemGoal(String id, String data) {
      super(id, data);
   }

   public EntityType<?> getMob() {
      return EntityType.IRON_GOLEM;
   }

   public String getGoalName() {
      return "Spy on Iron Golem";
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
