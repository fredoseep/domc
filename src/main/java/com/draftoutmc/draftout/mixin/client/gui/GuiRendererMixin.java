package com.draftoutmc.draftout.mixin.client.gui;

import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.joml.Matrix3x2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiRenderer.class})
public class GuiRendererMixin {
   @Final
   @Shadow
   private GuiRenderState renderState;
   @Unique
   private float maxItemPoseScale = 1.0F;
   @Unique
   private float cachedMaxItemPoseScale = 1.0F;

   @Shadow
   private void invalidateItemAtlas() {
   }

   @Inject(
      method = {"prepareItemElements"},
      at = {@At("HEAD")}
   )
   private void lockout$computeMaxPoseScale(CallbackInfo ci) {
      float[] max = new float[]{1.0F};
      this.renderState.forEachItem((itemState) -> {
         Matrix3x2f pose = itemState.pose();
         float scale = (float)Math.sqrt((double)(pose.m00 * pose.m00 + pose.m01 * pose.m01));
         if (scale > max[0]) {
            max[0] = scale;
         }

      });
      if (max[0] != this.cachedMaxItemPoseScale) {
         this.invalidateItemAtlas();
         this.cachedMaxItemPoseScale = max[0];
      }

      this.maxItemPoseScale = max[0];
   }

   @ModifyArg(
      method = {"prepareItemElements"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/render/GuiRenderer;prepareItemAtlas(Ljava/util/Set;I)Lnet/minecraft/client/gui/render/GuiItemAtlas;"
),
      index = 1
   )
   private int lockout$scaleAtlasSlotSize(int slotTextureSize) {
      return Math.round((float)slotTextureSize * this.maxItemPoseScale);
   }
}
