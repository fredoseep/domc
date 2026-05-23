package com.draftoutmc.draftout.mixin.client.export;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.export.GoalIconExportRunner;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiGraphicsExtractor.class})
public abstract class GuiGraphicsExtractorExportMixin {
   @Inject(
      method = {"itemCount"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void draftout$drawExportItemCount(Font font, ItemStack stack, int x, int y, @Nullable String count, CallbackInfo ci) {
      if (GoalIconExportRunner.isExportMode()) {
         if (stack.getCount() == 1 && count == null) {
            ci.cancel();
         } else {
            Utility.drawStackCount((GuiGraphicsExtractor)(Object)this, x, y, count == null ? String.valueOf(stack.getCount()) : count);
            ci.cancel();
         }
      }
   }
}
