package com.draftoutmc.draftout.lockout.goals.advancement.unique;

import com.draftoutmc.draftout.lockout.interfaces.GetUniqueAdvancementsGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;

public class Get30UniqueAdvancementsGoal extends GetUniqueAdvancementsGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/advancements/30_advancements.png");

   public Get30UniqueAdvancementsGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Get 30 Advancements";
   }

   public int getAmount() {
      return 30;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
