package com.draftoutmc.draftout.lockout.goals.dimension;

import com.draftoutmc.draftout.lockout.interfaces.EnterDimensionGoal;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class EnterEndGoal extends EnterDimensionGoal {
   private static final Item ITEM;

   public EnterEndGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Enter The End";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   public ResourceKey<Level> getWorldRegistryKey() {
      return ServerLevel.END;
   }

   static {
      ITEM = Items.END_PORTAL_FRAME;
   }
}
