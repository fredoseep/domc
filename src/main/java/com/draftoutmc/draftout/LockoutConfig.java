package com.draftoutmc.draftout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import lombok.Generated;

public class LockoutConfig {
   private static final Path CONFIG_PATH = (new File("./config/lockout.json")).toPath();
   private static LockoutConfig instance;
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   @SerializedName("board position")
   public BoardPosition boardPosition;
   @SerializedName("cycle speed")
   public int cycleSpeed;

   public LockoutConfig() {
      this.boardPosition = LockoutConfig.BoardPosition.RIGHT;
      this.cycleSpeed = 3000;
   }

   public static void load() {
      if (!Files.exists(CONFIG_PATH, new LinkOption[0])) {
         createConfigDir();
         loadDefaultConfig();
         save();
      } else {
         try {
            String s = Files.readString(CONFIG_PATH);
            instance = (LockoutConfig)GSON.fromJson(s, LockoutConfig.class);
            save();
         } catch (Exception var1) {
            Lockout.log("Invalid config file, using default values.");
            loadDefaultConfig();
         }
      }

   }

   public static void loadDefaultConfig() {
      instance = new LockoutConfig();
      instance.boardPosition = LockoutConfig.BoardPosition.RIGHT;
      instance.cycleSpeed = 3000;
   }

   private static void createConfigDir() {
      try {
         Files.createDirectory(Path.of("./config"));
      } catch (Exception e) {
         Lockout.error(e);
      }

   }

   public static void save() {
      try {
         Files.writeString(CONFIG_PATH, GSON.toJson(instance));
      } catch (Exception e) {
         Lockout.error(e);
      }

   }

   @Generated
   public static LockoutConfig getInstance() {
      return instance;
   }

   public static enum BoardPosition {
      @SerializedName("left")
      LEFT,
      @SerializedName("right")
      RIGHT;

      public static BoardPosition match(String boardPosition) {
         BoardPosition var10000;
         switch (boardPosition) {
            case "left" -> var10000 = LEFT;
            case "right" -> var10000 = RIGHT;
            default -> var10000 = null;
         }

         return var10000;
      }

      // $FF: synthetic method
      private static BoardPosition[] $values() {
         return new BoardPosition[]{LEFT, RIGHT};
      }
   }
}
