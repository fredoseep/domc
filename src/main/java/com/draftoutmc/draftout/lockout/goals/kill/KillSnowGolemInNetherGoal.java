package com.draftoutmc.draftout.lockout.goals.kill;

import net.minecraft.resources.Identifier;

public class KillSnowGolemInNetherGoal extends KillSnowGolemGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill/kill_snowman_nether.png");

   public KillSnowGolemInNetherGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Kill Snow Golem in The Nether";
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
