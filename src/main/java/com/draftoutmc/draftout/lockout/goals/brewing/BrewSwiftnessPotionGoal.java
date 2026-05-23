package com.draftoutmc.draftout.lockout.goals.brewing;

import com.draftoutmc.draftout.lockout.interfaces.ObtainPotionItemGoal;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class BrewSwiftnessPotionGoal extends ObtainPotionItemGoal {
   private static final List<Holder<Potion>> POTION_LIST;

   public BrewSwiftnessPotionGoal(String id, String data) {
      super(id, data);
   }

   public List<Holder<Potion>> getPotions() {
      return POTION_LIST;
   }

   public String getGoalName() {
      return "Brew a Potion of Swiftness";
   }

   static {
      POTION_LIST = List.of(Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS);
   }
}
