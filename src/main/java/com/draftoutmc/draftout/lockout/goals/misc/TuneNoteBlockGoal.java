package com.draftoutmc.draftout.lockout.goals.misc;

import com.draftoutmc.draftout.lockout.interfaces.IncrementStatGoal;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class TuneNoteBlockGoal extends IncrementStatGoal {
   private static final List<Identifier> STATS;
   private final ItemStack ITEM_STACK;

   public TuneNoteBlockGoal(String id, String data) {
      super(id, data);
      this.ITEM_STACK = Items.NOTE_BLOCK.getDefaultInstance();
   }

   public List<Identifier> getStats() {
      return STATS;
   }

   public String getGoalName() {
      return "Tune a Note Block";
   }

   public @Nullable ItemStack getTextureItemStack() {
      return this.ITEM_STACK;
   }

   static {
      STATS = List.of(Stats.TUNE_NOTEBLOCK, Stats.PLAY_NOTEBLOCK);
   }
}
