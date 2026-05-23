package com.draftoutmc.draftout.client.export;

public final class GoalIconExportEnvironment {
   public static final String EXPORT_PROPERTY = "draftout.goalIcons.export";
   public static final String OUTPUT_DIR_PROPERTY = "draftout.goalIcons.outputDir";
   public static final String VERSION_PROPERTY = "draftout.goalIcons.version";
   public static final String RESOLUTION_PROPERTY = "draftout.goalIcons.resolution";
   public static final String LIMIT_PROPERTY = "draftout.goalIcons.limit";
   public static final String EXPORT_ENV = "DRAFTOUT_GOAL_ICON_EXPORT";
   public static final String OUTPUT_DIR_ENV = "DRAFTOUT_GOAL_ICON_OUTPUT_DIR";
   public static final String VERSION_ENV = "DRAFTOUT_GOAL_ICON_VERSION";
   public static final String RESOLUTION_ENV = "DRAFTOUT_GOAL_ICON_RESOLUTION";
   public static final String LIMIT_ENV = "DRAFTOUT_GOAL_ICON_LIMIT";

   private GoalIconExportEnvironment() {
   }

   public static boolean isExportMode() {
      return Boolean.getBoolean("draftout.goalIcons.export") || "true".equalsIgnoreCase(System.getenv("DRAFTOUT_GOAL_ICON_EXPORT"));
   }

   public static String getString(String propertyName, String environmentName, String defaultValue) {
      String propertyValue = System.getProperty(propertyName);
      if (propertyValue != null && !propertyValue.isBlank()) {
         return propertyValue;
      } else {
         String environmentValue = System.getenv(environmentName);
         return environmentValue != null && !environmentValue.isBlank() ? environmentValue : defaultValue;
      }
   }

   public static int getInt(String propertyName, String environmentName, int defaultValue) {
      String value = getString(propertyName, environmentName, (String)null);
      if (value == null) {
         return defaultValue;
      } else {
         try {
            return Integer.parseInt(value);
         } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value for " + propertyName + "/" + environmentName + ": " + value, e);
         }
      }
   }
}
