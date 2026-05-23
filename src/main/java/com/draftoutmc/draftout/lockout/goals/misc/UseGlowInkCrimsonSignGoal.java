package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.interfaces.UseGlowInkSignGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseGlowInkCrimsonSignGoal extends UseGlowInkSignGoal implements TextureProvider {
   private static final List<Item> ITEMS;
   private static final Identifier TEXTURE;

   public UseGlowInkCrimsonSignGoal(String id, String data) {
      super(id, data);
   }

   public List<Item> getSignItems() {
      return ITEMS;
   }

   public String getGoalName() {
      return "Use Glow Ink on Crimson Sign";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   static {
      ITEMS = List.of(Items.CRIMSON_SIGN, Items.CRIMSON_HANGING_SIGN);
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/glow_crimson_sign.png");
   }
}
