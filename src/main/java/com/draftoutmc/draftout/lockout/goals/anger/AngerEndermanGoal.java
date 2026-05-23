package com.draftoutmc.draftout.lockout.goals.anger;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class AngerEndermanGoal extends Goal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/enrage_enderman.png");

   public AngerEndermanGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Enrage Enderman";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
