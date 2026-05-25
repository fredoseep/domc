package com.draftoutmc.draftout.match.data;

import com.draftoutmc.draftout.Lockout;
import lombok.Generated;

public class RankedData {
   private static String linkedTwitchUsername;
   private static PlayerProfile myProfile;
   private static String worldName;
   private static Lockout lockout;

   @Generated
   public static String linkedTwitchUsername() {
      return linkedTwitchUsername;
   }

   @Generated
   public static void linkedTwitchUsername(String linkedTwitchUsername) {
      RankedData.linkedTwitchUsername = linkedTwitchUsername;
   }

   @Generated
   public static PlayerProfile myProfile() {
      return myProfile;
   }

   @Generated
   public static void myProfile(PlayerProfile myProfile) {
      RankedData.myProfile = myProfile;
   }
   @Generated
   public static String worldName() {
      return worldName;
   }

   @Generated
   public static void worldName(String worldName) {
      RankedData.worldName = worldName;
   }

   @Generated
   public static Lockout lockout() {
      return lockout;
   }

   @Generated
   public static void lockout(Lockout lockout) {
      RankedData.lockout = lockout;
   }
}
