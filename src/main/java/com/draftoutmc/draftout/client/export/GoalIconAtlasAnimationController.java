package com.draftoutmc.draftout.client.export;

import com.draftoutmc.draftout.mixin.client.export.SpriteAnimationStateAccessor;
import com.draftoutmc.draftout.mixin.client.export.TextureAtlasAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;

final class GoalIconAtlasAnimationController {
   private GoalIconAtlasAnimationController() {
   }

   static void setAnimationTick(int tick) {
      setAnimationTick(TextureAtlas.LOCATION_BLOCKS, tick);
      setAnimationTick(TextureAtlas.LOCATION_ITEMS, tick);
   }

   private static void setAnimationTick(Identifier atlasId, int tick) {
      AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(atlasId);
      if (texture instanceof TextureAtlas atlas) {
         TextureAtlasAccessor atlasAccessor = (TextureAtlasAccessor)atlas;

         for(SpriteContents.AnimationState animationState : atlasAccessor.draftout$getAnimatedTexturesStates()) {
            SpriteAnimationStateAccessor animationAccessor = (SpriteAnimationStateAccessor)animationState;
            animationAccessor.draftout$setFrame(0);
            animationAccessor.draftout$setSubFrame(0);
            animationAccessor.draftout$setDirty(true);

            for(int i = 0; i < tick; ++i) {
               animationState.tick();
            }
         }

         atlasAccessor.draftout$uploadAnimationFrames();
      }
   }
}
