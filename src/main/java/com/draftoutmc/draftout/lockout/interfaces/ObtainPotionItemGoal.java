package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jspecify.annotations.Nullable;

public abstract class ObtainPotionItemGoal extends Goal {
   private final ItemStack ITEM;

   public ObtainPotionItemGoal(String id, String data) {
      super(id, data);
      this.ITEM = PotionContents.createItemStack(Items.POTION, (Holder)this.getPotions().getFirst());
   }

   public abstract List<Holder<Potion>> getPotions();

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM;
   }
}
