package com.draftoutmc.draftout.lockout.goals.brewing;

import com.draftoutmc.draftout.lockout.interfaces.ObtainPotionItemGoal;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class BrewWeaknessPotionGoal extends ObtainPotionItemGoal {
   private static final List<Holder<Potion>> POTION_LIST;

   public BrewWeaknessPotionGoal(String id, String data) {
      super(id, data);
   }

   public List<Holder<Potion>> getPotions() {
      return POTION_LIST;
   }

   public String getGoalName() {
      return "Brew a Potion of Weakness";
   }

   static {
      POTION_LIST = List.of(Potions.WEAKNESS, Potions.LONG_WEAKNESS);
   }
}
