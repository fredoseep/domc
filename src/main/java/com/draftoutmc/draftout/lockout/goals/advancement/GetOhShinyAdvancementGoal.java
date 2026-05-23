package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class GetOhShinyAdvancementGoal extends AdvancementGoal implements TextureProvider {
   private final ItemStack ITEM_STACK;
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "nether/distract_piglin"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/advancements/oh_shiny.png");

   public GetOhShinyAdvancementGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.GOLD_INGOT.getDefaultInstance();
   }

   public String getGoalName() {
      return "Obtain \"Oh Shiny\" Advancement";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
