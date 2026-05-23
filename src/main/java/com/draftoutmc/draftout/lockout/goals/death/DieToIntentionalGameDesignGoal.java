package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToDamageTypeGoal;
import com.draftoutmc.draftout.lockout.texture.CycleTexturesProvider;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public class DieToIntentionalGameDesignGoal extends DieToDamageTypeGoal implements CycleTexturesProvider {
   private static final List<Identifier> TEXTURES = List.of(Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_anchor.png"), Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_bed.png"));

   public DieToIntentionalGameDesignGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die to [Intentional Game Design]";
   }

   public List<ResourceKey<DamageType>> getDamageRegistryKeys() {
      return List.of(DamageTypes.BAD_RESPAWN_POINT);
   }

   public Identifier getTextureIdentifier() {
      return null;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      return CycleTexturesProvider.super.renderTexture(context, x, y, tick);
   }

   public List<Identifier> getTexturesToDisplay() {
      return TEXTURES;
   }
}
