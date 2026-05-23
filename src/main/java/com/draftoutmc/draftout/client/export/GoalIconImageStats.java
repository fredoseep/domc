package com.draftoutmc.draftout.client.export;

record GoalIconImageStats(int nonTransparentPixels, int minX, int minY, int maxX, int maxY) {
   String bounds() {
      return this.nonTransparentPixels == 0 ? "empty" : this.minX + "," + this.minY + "-" + this.maxX + "," + this.maxY;
   }
}
