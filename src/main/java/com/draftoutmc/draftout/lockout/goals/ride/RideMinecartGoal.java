package com.draftoutmc.draftout.lockout.goals.ride;

import com.draftoutmc.draftout.lockout.interfaces.RideEntityGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class RideMinecartGoal extends RideEntityGoal implements TextureProvider {
   private final ItemStack ITEM_STACK;
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/ride_minecart.png");

   public RideMinecartGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.MINECART.getDefaultInstance();
   }

   public EntityType<?> getEntityType() {
      return EntityType.MINECART;
   }

   public String getGoalName() {
      return "Ride a Minecart";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
