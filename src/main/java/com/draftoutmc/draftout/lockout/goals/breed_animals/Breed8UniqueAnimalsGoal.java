package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedUniqueAnimalsGoal;

public class Breed8UniqueAnimalsGoal extends BreedUniqueAnimalsGoal {
   public Breed8UniqueAnimalsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 8;
   }
}
