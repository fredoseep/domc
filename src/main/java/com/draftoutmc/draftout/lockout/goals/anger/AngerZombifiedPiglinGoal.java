package com.draftoutmc.draftout.lockout.goals.anger;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class AngerZombifiedPiglinGoal extends Goal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/enrage_zombie_piglin.png");

   public AngerZombifiedPiglinGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Enrage Zombified Piglin";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
