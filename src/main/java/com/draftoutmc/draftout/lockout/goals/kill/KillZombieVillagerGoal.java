package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.lockout.interfaces.KillMobGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class KillZombieVillagerGoal extends KillMobGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/kill/kill_zombie_villager.png");

   public KillZombieVillagerGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Kill Zombie Villager";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public EntityType<?> getEntity() {
      return EntityType.ZOMBIE_VILLAGER;
   }
}
