package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class TootGoatHornGoal extends Goal implements TextureProvider {
   private final ItemStack ITEM_STACK;
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/item/goat_horn.png");

   public TootGoatHornGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.GOAT_HORN.getDefaultInstance();
   }

   public String getGoalName() {
      return "Toot a Goat Horn";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
