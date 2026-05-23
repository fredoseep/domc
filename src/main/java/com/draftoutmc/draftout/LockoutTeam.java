package com.draftoutmc.draftout;

import java.util.List;
import lombok.Generated;

public class LockoutTeam {
   private final List<String> players;
   private final int color;
   private int points = 0;
   private boolean forfeited = false;

   public LockoutTeam(List<String> playerNames, int color) {
      this.players = playerNames;
      this.color = color;
   }

   public List<String> getPlayerNames() {
      return this.players;
   }

   public String getDisplayName() {
      return (String)this.players.getFirst();
   }

   public void addPoint() {
      ++this.points;
   }

   public void takePoint() {
      --this.points;
   }

   @Generated
   public int getColor() {
      return this.color;
   }

   @Generated
   public int getPoints() {
      return this.points;
   }

   @Generated
   public boolean isForfeited() {
      return this.forfeited;
   }

   @Generated
   public void setForfeited(boolean forfeited) {
      this.forfeited = forfeited;
   }
}
