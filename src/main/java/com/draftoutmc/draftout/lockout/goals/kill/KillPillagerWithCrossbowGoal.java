package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class KillPillagerWithCrossbowGoal extends AdvancementGoal implements TextureProvider {
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "adventure/whos_the_pillager_now"));
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill/kill_pillager_crossbow.png");

   public KillPillagerWithCrossbowGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public String getGoalName() {
      return "Kill Pillager using Crossbow";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
