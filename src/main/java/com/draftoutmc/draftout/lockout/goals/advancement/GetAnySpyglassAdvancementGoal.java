package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GetAnySpyglassAdvancementGoal extends AdvancementGoal implements TextureProvider {
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "adventure/spyglass_at_parrot"), Identifier.fromNamespaceAndPath("minecraft", "adventure/spyglass_at_ghast"), Identifier.fromNamespaceAndPath("minecraft", "adventure/spyglass_at_dragon"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/advancements/spyglass.png");

   public GetAnySpyglassAdvancementGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Get any Spyglass Advancement";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
