package com.draftoutmc.draftout.lockout.goals.workstation;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseComposterGoal extends Goal implements TextureProvider {
   private final ItemStack ITEM_STACK;
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/filled_composter.png");

   public UseComposterGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.COMPOSTER.getDefaultInstance();
   }

   public String getGoalName() {
      return "Fill Composter and get Bone Meal";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
