package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class GetWhatADealAdvancementGoal extends AdvancementGoal {
   private final ItemStack ITEM_STACK;
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "adventure/trade"));

   public GetWhatADealAdvancementGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.EMERALD.getDefaultInstance();
   }

   public String getGoalName() {
      return "Trade with a Villager";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }
}
