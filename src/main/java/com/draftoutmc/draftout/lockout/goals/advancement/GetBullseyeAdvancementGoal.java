package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GetBullseyeAdvancementGoal extends AdvancementGoal implements TextureProvider {
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "adventure/bullseye"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/advancements/bullseye.png");

   public GetBullseyeAdvancementGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public String getGoalName() {
      return "Obtain \"Bullseye\" Advancement";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
