package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class GetWaxOnAdvancementGoal extends AdvancementGoal {
   private static final Item ITEM;
   private static final List<Identifier> ADVANCEMENTS;

   public GetWaxOnAdvancementGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public String getGoalName() {
      return "Wax a Copper Block";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   static {
      ITEM = Items.HONEYCOMB;
      ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "husbandry/wax_on"));
   }
}
