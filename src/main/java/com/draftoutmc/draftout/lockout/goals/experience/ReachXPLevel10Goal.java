package com.draftoutmc.draftout.lockout.goals.experience;

import com.draftoutmc.draftout.lockout.interfaces.ReachXPLevelGoal;
import net.minecraft.resources.Identifier;

public class ReachXPLevel10Goal extends ReachXPLevelGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/experience/level_10.png");

   public ReachXPLevel10Goal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Reach XP level 10";
   }

   public int getAmount() {
      return 10;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
