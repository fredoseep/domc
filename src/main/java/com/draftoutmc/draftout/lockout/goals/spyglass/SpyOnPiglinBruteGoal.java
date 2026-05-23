package com.draftoutmc.draftout.lockout.goals.spyglass;

import com.draftoutmc.draftout.lockout.interfaces.SpyOnMobGoal;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class SpyOnPiglinBruteGoal extends SpyOnMobGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/spy/spy_piglin_brute.png");

   public SpyOnPiglinBruteGoal(String id, String data) {
      super(id, data);
   }

   public EntityType<?> getMob() {
      return EntityType.PIGLIN_BRUTE;
   }

   public String getGoalName() {
      return "Spy on Piglin Brute";
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
