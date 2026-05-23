package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GetHiredHelpAdvancementGoal extends AdvancementGoal implements TextureProvider {
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "adventure/summon_iron_golem"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/construct_iron_golem.png");

   public GetHiredHelpAdvancementGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public String getGoalName() {
      return "Construct an Iron Golem";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
