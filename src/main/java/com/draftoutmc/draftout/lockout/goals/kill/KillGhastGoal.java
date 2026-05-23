package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.lockout.interfaces.KillMobGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class KillGhastGoal extends KillMobGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill/kill_ghast.png");

   public KillGhastGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Kill Ghast";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public EntityType<?> getEntity() {
      return EntityType.GHAST;
   }
}
