package com.draftoutmc.draftout.lockout.icon;

import com.draftoutmc.draftout.client.LockoutClient;
import com.draftoutmc.draftout.lockout.Goal;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jspecify.annotations.Nullable;

public record GoalIconFrame(List<GoalIconRenderer> subFrameRenderers, @Nullable Integer subFrameCycleMs, boolean enchanted) {
   public GoalIconFrame(List<GoalIconRenderer> subFrameRenderers, @Nullable Integer subFrameCycleMs, boolean enchanted) {
      if (subFrameRenderers.isEmpty()) {
         throw new IllegalArgumentException("Goal icon frame must have at least one sub-frame renderer");
      } else if (subFrameCycleMs != null && subFrameCycleMs <= 0) {
         throw new IllegalArgumentException("Goal icon sub-frame cycle duration must be positive");
      } else {
         subFrameRenderers = List.copyOf(subFrameRenderers);
         this.subFrameRenderers = subFrameRenderers;
         this.subFrameCycleMs = subFrameCycleMs;
         this.enchanted = enchanted;
      }
   }

   public static GoalIconFrame of(GoalIconRenderer renderer) {
      return new GoalIconFrame(List.of(renderer), (Integer)null, false);
   }

   public static GoalIconFrame of(GoalIconRenderer renderer, boolean enchanted) {
      return new GoalIconFrame(List.of(renderer), (Integer)null, enchanted);
   }

   public static GoalIconFrame animatedGoalTicks(Goal goal, int subFrameCycleMs, int... ticks) {
      return new GoalIconFrame(IntStream.of(ticks).mapToObj((tick) -> rendererForGoalTick(goal, tick)).toList(), subFrameCycleMs, false);
   }

   public static GoalIconFrame renderGoalAtTick(Goal goal, int tick) {
      return of(rendererForGoalTick(goal, tick));
   }

   public static GoalIconFrame item(Item item) {
      return item(item.getDefaultInstance(), false);
   }

   public static GoalIconFrame item(Item item, boolean enchanted) {
      return item(item.getDefaultInstance(), enchanted);
   }

   public static GoalIconFrame item(ItemStack stack, boolean enchanted) {
      return of(rendererForItem(stack), enchanted);
   }

   public static GoalIconFrame dyedItem(Item item, int color) {
      ItemStack stack = item.getDefaultInstance();
      stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color));
      return item(stack, false);
   }

   private static GoalIconRenderer rendererForGoalTick(Goal goal, int tick) {
      return (context, font, x, y) -> {
         int previousTick = LockoutClient.RENDER_TICK;
         LockoutClient.RENDER_TICK = tick;

         try {
            goal.render(context, font, x, y);
         } finally {
            LockoutClient.RENDER_TICK = previousTick;
         }

      };
   }

   private static GoalIconRenderer rendererForItem(ItemStack stack) {
      return (context, font, x, y) -> context.item(stack.copy(), x, y);
   }
}
