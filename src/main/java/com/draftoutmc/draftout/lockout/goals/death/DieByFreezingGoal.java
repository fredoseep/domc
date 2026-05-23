package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToDamageTypeGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public class DieByFreezingGoal extends DieToDamageTypeGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_freezing.png");

   public DieByFreezingGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die by Freezing";
   }

   public List<ResourceKey<DamageType>> getDamageRegistryKeys() {
      return List.of(DamageTypes.FREEZE);
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
