package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class GetLocalBreweryAdvancementGoal extends AdvancementGoal {
   private final ItemStack ITEM_STACK;
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "nether/brew_potion"));

   public GetLocalBreweryAdvancementGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.BREWING_STAND.getDefaultInstance();
   }

   public String getGoalName() {
      return "Use Brewing Stand";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }
}
