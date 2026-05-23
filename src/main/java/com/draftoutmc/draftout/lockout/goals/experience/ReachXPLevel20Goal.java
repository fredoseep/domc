package com.draftoutmc.draftout.lockout.goals.experience;

import com.draftoutmc.draftout.lockout.interfaces.ReachXPLevelGoal;
import net.minecraft.resources.Identifier;

public class ReachXPLevel20Goal extends ReachXPLevelGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/experience/level_20.png");

   public ReachXPLevel20Goal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Reach XP level 20";
   }

   public int getAmount() {
      return 20;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
