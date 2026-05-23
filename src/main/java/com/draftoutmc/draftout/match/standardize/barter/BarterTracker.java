package com.draftoutmc.draftout.match.standardize.barter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BarterTracker {
   private static final int FIRST_TRADES = 90;
   private static final int GUARANTEED_TRADES = 6;
   private static Set<Integer> pearlSlots = new HashSet();

   public static void initialize(long worldSeed) {
      List<Integer> pool = new ArrayList();

      for(int i = 0; i < 90; ++i) {
         pool.add(i);
      }

      Random random = new Random(worldSeed ^ -2977037099943890809L);
      Collections.shuffle(pool, random);
      pearlSlots = new HashSet(pool.subList(0, 6));
   }

   public static Optional<List<ItemStack>> getOverrideItems(int tradeIndex, long worldSeed) {
      if (tradeIndex < 90 && pearlSlots.contains(tradeIndex)) {
         Random random = new Random(worldSeed ^ (long)tradeIndex ^ -2977037099943890809L);
         int count = random.nextInt(2, 5);
         return Optional.of(List.of(new ItemStack(Items.ENDER_PEARL, count)));
      } else {
         return Optional.empty();
      }
   }
}
