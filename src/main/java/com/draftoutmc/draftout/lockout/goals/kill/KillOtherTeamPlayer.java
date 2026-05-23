package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class KillOtherTeamPlayer extends Goal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill_player.png");

   public KillOtherTeamPlayer(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Kill another team's player";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
