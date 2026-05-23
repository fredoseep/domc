package com.draftoutmc.draftout.lockout.goals.advancement.unique;

import com.draftoutmc.draftout.lockout.interfaces.GetUniqueAdvancementsGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;

public class Get10UniqueAdvancementsGoal extends GetUniqueAdvancementsGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/advancements/10_advancements.png");

   public Get10UniqueAdvancementsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Get 10 Advancements";
   }

   public int getAmount() {
      return 10;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
