package com.draftoutmc.draftout.match.data;

import lombok.Generated;

public class RankedData {
   private static String linkedTwitchUsername;
   public static PlayerProfile myProfile;

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
}
