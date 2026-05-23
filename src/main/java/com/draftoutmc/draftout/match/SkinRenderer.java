package com.draftoutmc.draftout.match;

import com.draftoutmc.draftout.Lockout;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;

public class SkinRenderer {
   private final TextureManager textureManager;
   private final Map<UUID, Identifier> registeredSkins = new HashMap();
   private final Identifier FALLBACK = Identifier.fromNamespaceAndPath("draftout", "textures/gui/sprites/default_head.png");

   public SkinRenderer(TextureManager textureManager) {
      this.textureManager = textureManager;
   }

   public Identifier getMySkin() {
      UUID uuid = Minecraft.getInstance().getUser().getProfileId();
      if (this.registeredSkins.containsKey(uuid)) {
         return (Identifier)this.registeredSkins.get(uuid);
      } else {
         try {
            Identifier skinTexture = Minecraft.getInstance().player.getSkin().body().texturePath();
            AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(skinTexture);
            if (!(abstractTexture instanceof DynamicTexture)) {
               return this.FALLBACK;
            } else {
               DynamicTexture texture = (DynamicTexture)abstractTexture;
               NativeImage skinImage = texture.getPixels();
               NativeImage head = new NativeImage(8, 8, true);

               for(int x = 0; x < 8; ++x) {
                  for(int y = 0; y < 8; ++y) {
                     int baseColor = skinImage.getPixel(8 + x, 8 + y);
                     int hatColor = skinImage.getPixel(40 + x, 8 + y);
                     head.setPixel(x, y, this.blendColors(baseColor, hatColor));
                  }
               }

               Identifier location = Identifier.fromNamespaceAndPath("draftout", "skins/head_" + uuid.toString().toLowerCase());
               this.textureManager.register(location, new DynamicTexture(() -> "head_" + String.valueOf(uuid), head));
               this.registeredSkins.put(uuid, location);
               return location;
            }
         } catch (Exception var11) {
            return null;
         }
      }
   }

   public Identifier getSkinTexture(UUID uuid, String base64Data) {
      if (this.registeredSkins.containsKey(uuid)) {
         return (Identifier)this.registeredSkins.get(uuid);
      } else if (base64Data != null && !base64Data.isEmpty()) {
         byte[] bytes;
         try {
            String cleanData = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            bytes = Base64.getDecoder().decode(cleanData);
         } catch (Exception e) {
            Lockout.error(e);
            return this.FALLBACK;
         }

         try {
            NativeImage image = NativeImage.read(new ByteArrayInputStream(bytes));
            DynamicTexture texture = new DynamicTexture(() -> "skin_" + String.valueOf(uuid), image);
            Identifier location = Identifier.fromNamespaceAndPath("draftout", "skins/" + uuid.toString().toLowerCase());
            this.textureManager.register(location, texture);
            this.registeredSkins.put(uuid, location);
            return location;
         } catch (Exception e) {
            Lockout.error("Could not load skin", e);
            return this.FALLBACK;
         }
      } else {
         return this.FALLBACK;
      }
   }

   private int blendColors(int base, int overlay) {
      int overlayAlpha = overlay >> 24 & 255;
      if (overlayAlpha == 0) {
         return base;
      } else if (overlayAlpha == 255) {
         return overlay;
      } else {
         float a = (float)overlayAlpha / 255.0F;
         int r = Math.round((float)(overlay >> 16 & 255) * a + (float)(base >> 16 & 255) * (1.0F - a));
         int g = Math.round((float)(overlay >> 8 & 255) * a + (float)(base >> 8 & 255) * (1.0F - a));
         int b = Math.round((float)(overlay & 255) * a + (float)(base & 255) * (1.0F - a));
         return -16777216 | r << 16 | g << 8 | b;
      }
   }
}
