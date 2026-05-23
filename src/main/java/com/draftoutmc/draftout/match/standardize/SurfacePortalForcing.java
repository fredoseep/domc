package com.draftoutmc.draftout.match.standardize;

import lombok.Generated;

public final class SurfacePortalForcing {
   private static boolean active = false;
   private static boolean exited = false;

   public static void activate() {
      active = true;
   }

   public static void deactivate() {
      active = false;
   }

   public static boolean hasExited() {
      return exited;
   }

   public static void markExited() {
      exited = true;
   }

   public static void reset() {
      exited = false;
      active = false;
   }

   @Generated
   public static boolean isActive() {
      return active;
   }
}
