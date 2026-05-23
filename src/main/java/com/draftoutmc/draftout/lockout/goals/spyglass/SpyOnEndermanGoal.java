package com.draftoutmc.draftout.lockout.goals.spyglass;

import com.draftoutmc.draftout.lockout.interfaces.SpyOnMobGoal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class SpyOnEndermanGoal extends SpyOnMobGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/spy/spy_enderman.png");

   public SpyOnEndermanGoal(String id, String data) {
      super(id, data);
   }

   public EntityType<?> getMob() {
      return EntityType.ENDERMAN;
   }

   public String getGoalName() {
      return "Spy on Enderman";
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
