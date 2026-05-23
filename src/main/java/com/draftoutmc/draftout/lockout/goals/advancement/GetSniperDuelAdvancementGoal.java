package com.draftoutmc.draftout.lockout.goals.advancement;

import com.draftoutmc.draftout.lockout.interfaces.AdvancementGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class GetSniperDuelAdvancementGoal extends AdvancementGoal {
   private static final Item ITEM;
   private static final List<Identifier> ADVANCEMENTS;

   public GetSniperDuelAdvancementGoal(String id, String data) {
      super(id, data);
   }

   public List<Identifier> getAdvancements() {
      return ADVANCEMENTS;
   }

   public String getGoalName() {
      return "Obtain \"Sniper Duel\" Advancement";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   static {
      ITEM = Items.BOW;
      ADVANCEMENTS = List.of(Identifier.fromNamespaceAndPath("minecraft", "adventure/sniper_duel"));
   }
}
