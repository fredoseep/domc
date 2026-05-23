package com.draftoutmc.draftout.lockout.goals.death;

import com.draftoutmc.draftout.lockout.interfaces.DieToDamageTypeGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

public class DieToVoidGoal extends DieToDamageTypeGoal {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/death/die_to_void.png");

   public DieToVoidGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Die by falling in Void";
   }

   public List<ResourceKey<DamageType>> getDamageRegistryKeys() {
      return List.of(DamageTypes.FELL_OUT_OF_WORLD);
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
