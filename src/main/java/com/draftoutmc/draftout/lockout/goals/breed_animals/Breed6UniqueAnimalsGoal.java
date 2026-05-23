package com.draftoutmc.draftout.lockout.goals.breed_animals;

import com.draftoutmc.draftout.lockout.interfaces.BreedUniqueAnimalsGoal;

public class Breed6UniqueAnimalsGoal extends BreedUniqueAnimalsGoal {
   public Breed6UniqueAnimalsGoal(String id, String data) {
      super(id, data);
   }

   public int getAmount() {
      return 6;
   }
}
