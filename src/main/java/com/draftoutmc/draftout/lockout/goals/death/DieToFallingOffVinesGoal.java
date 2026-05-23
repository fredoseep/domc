package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.texture.CycleTexturesProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class DieToFallingOffVinesGoal extends Goal implements CycleTexturesProvider {
   private static final List<Identifier> TEXTURES = List.of(Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_vines.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_twisting_vines.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_weeping_vines.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_cave_vines.png"));

   public DieToFallingOffVinesGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die by falling off Vines";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public List<Identifier> getTexturesToDisplay() {
      return TEXTURES;
   }
}
