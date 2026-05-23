package com.draftoutmc.draftout.match.data;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import net.minecraft.util.ARGB;

public enum RankedRank {
   COAL("Coal", 0, ARGB.color(255, RankedRank.Colors.COAL_COLOR.getRGB())),
   IRON_1("Iron I", 500, ARGB.color(255, RankedRank.Colors.IRON_COLOR.getRGB())),
   IRON_2("Iron II", 600, ARGB.color(255, RankedRank.Colors.IRON_COLOR.getRGB())),
   IRON_3("Iron III", 700, ARGB.color(255, RankedRank.Colors.IRON_COLOR.getRGB())),
   GOLD_1("Gold I", 800, ARGB.color(255, RankedRank.Colors.GOLD_COLOR.getRGB())),
   GOLD_2("Gold II", 900, ARGB.color(255, RankedRank.Colors.GOLD_COLOR.getRGB())),
   GOLD_3("Gold III", 1000, ARGB.color(255, RankedRank.Colors.GOLD_COLOR.getRGB())),
   AMETHYST_1("Amethyst I", 1100, ARGB.color(255, RankedRank.Colors.AMETHYST_COLOR.getRGB())),
   AMETHYST_2("Amethyst II", 1200, ARGB.color(255, RankedRank.Colors.AMETHYST_COLOR.getRGB())),
   AMETHYST_3("Amethyst III", 1300, ARGB.color(255, RankedRank.Colors.AMETHYST_COLOR.getRGB())),
   DIAMOND_1("Diamond I", 1400, ARGB.color(255, RankedRank.Colors.DIAMOND_COLOR.getRGB())),
   DIAMOND_2("Diamond II", 1500, ARGB.color(255, RankedRank.Colors.DIAMOND_COLOR.getRGB())),
   DIAMOND_3("Diamond III", 1600, ARGB.color(255, RankedRank.Colors.DIAMOND_COLOR.getRGB())),
   GUARDIAN_1("Guardian I", 1700, ARGB.color(255, RankedRank.Colors.GUARDIAN_COLOR.getRGB())),
   GUARDIAN_2("Guardian II", 1800, ARGB.color(255, RankedRank.Colors.GUARDIAN_COLOR.getRGB())),
   GUARDIAN_3("Guardian III", 1900, ARGB.color(255, RankedRank.Colors.GUARDIAN_COLOR.getRGB())),
   WARDEN("Warden", 2000, ARGB.color(255, RankedRank.Colors.WARDEN_COLOR.getRGB()));

   private static final List<RankedRank> ranks = Arrays.stream(values()).toList().reversed();
   private final String displayName;
   private final int minElo;
   private final int color;

   public static RankedRank getFromElo(int elo) {
      for(RankedRank rank : ranks) {
         if (elo >= rank.minElo) {
            return rank;
         }
      }

      return COAL;
   }

   public RankedRank next() {
      for(RankedRank rank : values()) {
         if (rank.minElo > this.minElo) {
            return rank;
         }
      }

      return null;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }

   @Generated
   public int getMinElo() {
      return this.minElo;
   }

   @Generated
   public int getColor() {
      return this.color;
   }

   @Generated
   private RankedRank(final String displayName, final int minElo, final int color) {
      this.displayName = displayName;
      this.minElo = minElo;
      this.color = color;
   }

   // $FF: synthetic method
   private static RankedRank[] $values() {
      return new RankedRank[]{COAL, IRON_1, IRON_2, IRON_3, GOLD_1, GOLD_2, GOLD_3, AMETHYST_1, AMETHYST_2, AMETHYST_3, DIAMOND_1, DIAMOND_2, DIAMOND_3, GUARDIAN_1, GUARDIAN_2, GUARDIAN_3, WARDEN};
   }

   public static class Colors {
      public static final Color COAL_COLOR = new Color(4343366);
      public static final Color IRON_COLOR = new Color(12107198);
      public static final Color GOLD_COLOR = new Color(13867810);
      public static final Color AMETHYST_COLOR = new Color(12159950);
      public static final Color DIAMOND_COLOR = new Color(6791638);
      public static final Color GUARDIAN_COLOR = new Color(4548718);
      public static final Color WARDEN_COLOR = new Color(2835536);
   }
}
