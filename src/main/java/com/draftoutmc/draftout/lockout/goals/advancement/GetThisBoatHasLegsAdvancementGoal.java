package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class GetThisBoatHasLegsAdvancementGoal extends AdvancementGoal {
   private final ItemStack ITEM_STACK;
   private static final List<Identifier> ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "nether/ride_strider"));

   public GetThisBoatHasLegsAdvancementGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.WARPED_FUNGUS_ON_A_STICK.getDefaultInstance();
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public String getGoalName() {
      return "Obtain \"This boat has legs\" Advancement";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }
}
