package com.draftoutmc.draftout.mixin.server.draftout;

import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ZombieVillager.class})
public class ZombieVillagerFixMixin {
   @Redirect(
      method = {"defineSynchedData"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/entity/monster/zombie/ZombieVillager;initializeVillagerData()Lnet/minecraft/world/entity/npc/villager/VillagerData;"
)
   )
   private VillagerData draftout$skipBiomeLookupDuringConstruction(ZombieVillager instance) {
      return Villager.createDefaultVillagerData();
   }
}
