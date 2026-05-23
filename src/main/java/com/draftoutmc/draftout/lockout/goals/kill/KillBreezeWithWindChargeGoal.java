package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.lockout.interfaces.KillMobGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class KillBreezeWithWindChargeGoal extends KillMobGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill/breeze_wind_charge.png");

   public KillBreezeWithWindChargeGoal(String id, String data) {
      super(id, data);
   }

   public EntityType<?> getEntity() {
      return EntityType.BREEZE;
   }

   public String getGoalName() {
      return "Kill Breeze using Wind Charge";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
