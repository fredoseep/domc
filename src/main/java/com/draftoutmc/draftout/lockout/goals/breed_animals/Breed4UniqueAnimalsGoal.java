package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedUniqueAnimalsGoal;

public class Breed4UniqueAnimalsGoal extends BreedUniqueAnimalsGoal {
   public Breed4UniqueAnimalsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 4;
   }
}
