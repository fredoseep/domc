package com.draftoutmc.draftout.lockout.goals.kill.unique;

import com.draftoutmc.draftout.lockout.interfaces.KillUniqueHostileMobsGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class Kill10UniqueHostileMobsGoal extends KillUniqueHostileMobsGoal {
   private final ItemStack ITEM;

   public Kill10UniqueHostileMobsGoal(String id, String data) {
      super(id, data);
      this.ITEM = Items.IRON_SWORD.getDefaultInstance();
      this.ITEM.setCount(this.getAmount());
   }

   public String getGoalName() {
      return "Kill 10 Unique Hostile Mobs";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM;
   }

   public int getAmount() {
      return 10;
   }
}
