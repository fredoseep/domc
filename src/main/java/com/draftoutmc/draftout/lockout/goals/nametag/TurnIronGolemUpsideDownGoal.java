package com.draftoutmc.draftout.lockout.goals.nametag;

import com.draftoutmc.draftout.lockout.interfaces.NametagMobGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class TurnIronGolemUpsideDownGoal extends NametagMobGoal {
   private static final List<String> NAMES = List.of("Dinnerbone", "Grumm");
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/nametag/iron_golem.png");

   public TurnIronGolemUpsideDownGoal(String id, String data) {
      super(id, data);
   }

   public EntityType<?> getEntity() {
      return EntityType.IRON_GOLEM;
   }

   public List<String> getAcceptableNames() {
      return NAMES;
   }

   public String getGoalName() {
      return "Name an Iron Golem 'Dinnerbone'";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }
}
