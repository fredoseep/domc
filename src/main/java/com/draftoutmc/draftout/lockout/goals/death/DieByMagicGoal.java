package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToDamageTypeGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public class DieByMagicGoal extends DieToDamageTypeGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_magic.png");

   public DieByMagicGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die by Magic";
   }

   public List<ResourceKey<DamageType>> getDamageRegistryKeys() {
      return List.of(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC);
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
