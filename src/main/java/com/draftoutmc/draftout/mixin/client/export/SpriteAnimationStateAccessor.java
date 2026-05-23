package com.draftoutmc.draftout.mixin.client.export;

import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SpriteContents.AnimationState.class})
public interface SpriteAnimationStateAccessor {
   @Accessor("frame")
   void draftout$setFrame(int var1);

   @Accessor("subFrame")
   void draftout$setSubFrame(int var1);

   @Accessor("isDirty")
   void draftout$setDirty(boolean var1);
}
