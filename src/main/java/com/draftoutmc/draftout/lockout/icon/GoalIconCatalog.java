package com.draftoutmc.draftout.lockout.icon;

import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.GoalDataGenerator;
import com.draftoutmc.draftout.lockout.GoalRegistry;
import com.draftoutmc.draftout.lockout.texture.CycleItemTexturesProvider;
import com.draftoutmc.draftout.lockout.texture.CycleTexturesProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class GoalIconCatalog {
   public static final int DEFAULT_CYCLE_MS = 3000;

   private GoalIconCatalog() {
   }

   public static List<GoalIconEntry> collectEntries() {
      List<GoalIconEntry> entries = new ArrayList();

      for(String goalId : GoalRegistry.INSTANCE.getRegisteredGoals()) {
         List<String> dataList = GoalRegistry.INSTANCE.getDataGenerator(goalId).map(GoalDataGenerator::enumerateData).orElse(List.of("null"));
         for(String data : dataList) {
            Goal goal = GoalRegistry.INSTANCE.newGoal(goalId, data);
            if (goal == null) {
               throw new IllegalStateException("Could not instantiate goal icon entry " + goalId + " with data " + data);
            }

            entries.add(resolveEntry(goal));
         }
      }

      return entries;
   }

   private static GoalIconEntry resolveEntry(Goal goal) {
      Integer cycleMs = null;
      GoalIconAnimationMode animationMode = GoalIconAnimationMode.SEQUENTIAL;
      List<GoalIconFrame> frames;
      if (goal instanceof GoalIconFrameProvider provider) {
         frames = provider.getGoalIconFrames();
         cycleMs = provider.getGoalIconCycleMs();
         animationMode = provider.getGoalIconAnimationMode();
      } else {
         frames = defaultFrames(goal);
      }

      if (cycleMs == null && frames.size() > 1) {
         cycleMs = 3000;
      }

      if (frames.isEmpty()) {
         frames = List.of(GoalIconFrame.renderGoalAtTick(goal, 0));
      }

      return new GoalIconEntry(goal.getId(), goal.getData() == null ? "null" : goal.getData(), goal.getGoalName(), cycleMs, animationMode, frames);
   }

   private static List<GoalIconFrame> defaultFrames(Goal goal) {
      if (goal instanceof CycleItemTexturesProvider itemProvider) {
         List<Item> items = itemProvider.getItemsToDisplay();
         if (items != null && !items.isEmpty()) {
            return framesForItemCycle(goal, items);
         }
      }

      if (goal instanceof CycleTexturesProvider textureProvider) {
         List<Identifier> textures = textureProvider.getTexturesToDisplay();
         if (textures != null && !textures.isEmpty()) {
            return framesForCycle(goal, textures.size());
         }
      }

      return List.of(GoalIconFrame.renderGoalAtTick(goal, 0));
   }

   private static List<GoalIconFrame> framesForCycle(Goal goal, int frameCount) {
      return IntStream.range(0, frameCount).mapToObj((index) -> GoalIconFrame.renderGoalAtTick(goal, index * 60)).toList();
   }

   private static List<GoalIconFrame> framesForItemCycle(Goal goal, List<Item> items) {
      return framesForCycle(goal, items.size());
   }
}
