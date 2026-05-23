package com.draftoutmc.draftout.lockout.goals.tame_animal;

import com.draftoutmc.draftout.lockout.interfaces.TameAnimalGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class TameParrotGoal extends TameAnimalGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/tame/tame_parrot.png");
   private static final List<EntityType<?>> TYPES;

   public TameParrotGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Tame a Parrot";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public List<EntityType<?>> getAnimals() {
      return TYPES;
   }

   static {
      TYPES = List.of(EntityType.PARROT);
   }
}
