package com.draftoutmc.draftout.lockout.goals.consume;

import com.draftoutmc.draftout.lockout.interfaces.IncrementStatGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class EatCakeGoal extends IncrementStatGoal implements TextureProvider {
   private static final List<Identifier> STATS;
   private final ItemStack ITEM_STACK;
   private static final Identifier TEXTURE;

   public EatCakeGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.CAKE.getDefaultInstance();
   }

   public List<Identifier> getStats() {
      return STATS;
   }

   public String getGoalName() {
      return "Eat a slice of Cake";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   static {
      STATS = List.of(Stats.EAT_CAKE_SLICE);
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/eat_cake.png");
   }
}
