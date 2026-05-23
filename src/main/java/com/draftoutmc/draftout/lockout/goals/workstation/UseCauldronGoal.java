package com.draftoutmc.draftout.lockout.goals.workstation;

import com.draftoutmc.draftout.lockout.interfaces.IncrementStatGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class UseCauldronGoal extends IncrementStatGoal implements TextureProvider {
   private static final List<Identifier> STATS;
   private final ItemStack ITEM_STACK;
   private static final Identifier TEXTURE;

   public UseCauldronGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.ENCHANTING_TABLE.getDefaultInstance();
   }

   public List<Identifier> getStats() {
      return STATS;
   }

   public String getGoalName() {
      return "Use Cauldron to wash something";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   static {
      STATS = List.of(Stats.CLEAN_ARMOR, Stats.CLEAN_BANNER, Stats.CLEAN_SHULKER_BOX);
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/use_cauldron.png");
   }
}
