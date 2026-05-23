package com.draftoutmc.draftout.match.data;

import com.draftoutmc.draftout.match.SkinRenderer;
import com.google.gson.JsonObject;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class PlayerProfile {
   public static final SkinRenderer SKIN_RENDERER = new SkinRenderer(Minecraft.getInstance().getTextureManager());
   private final UUID uuid;
   private final String username;
   private final int elo;
   private final boolean ranked;
   private final int rank;
   private final Identifier skin;
   private final int preferredColor1;
   private final int preferredColor2;

   public PlayerProfile(UUID uuid, String username, int elo, boolean ranked, int rank, Identifier skin, int preferredColor1, int preferredColor2) {
      this.uuid = uuid;
      this.username = username;
      this.elo = elo;
      this.ranked = ranked;
      this.rank = rank;
      this.skin = skin;
      this.preferredColor1 = preferredColor1;
      this.preferredColor2 = preferredColor2;
   }

   public static PlayerProfile parse(JsonObject profileObject) {
      String username = profileObject.get("username").getAsString();
      UUID uuid = UUID.fromString(profileObject.get("uuid").getAsString());
      Identifier skin = SKIN_RENDERER.getSkinTexture(uuid, profileObject.get("skin") != null && !profileObject.get("skin").isJsonNull() ? profileObject.get("skin").getAsString() : null);
      return new PlayerProfile(uuid, username, profileObject.get("elo").getAsInt(), profileObject.get("ranked").getAsBoolean(), profileObject.get("rank") == null ? -1 : profileObject.get("rank").getAsInt(), skin, profileObject.get("preferredColor1").getAsInt(), profileObject.get("preferredColor2").getAsInt());
   }

   @Generated
   public UUID uuid() {
      return this.uuid;
   }

   @Generated
   public String username() {
      return this.username;
   }

   @Generated
   public int elo() {
      return this.elo;
   }

   @Generated
   public boolean ranked() {
      return this.ranked;
   }

   @Generated
   public int rank() {
      return this.rank;
   }

   @Generated
   public Identifier skin() {
      return this.skin;
   }

   @Generated
   public int preferredColor1() {
      return this.preferredColor1;
   }

   @Generated
   public int preferredColor2() {
      return this.preferredColor2;
   }
}
