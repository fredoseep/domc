package com.draftoutmc.draftout.mixin.client.gui;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.client.gui.BoardScreen;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({PauseScreen.class})
public class PauseScreenMixin<T extends LayoutElement> {
   @ModifyArg(
      method = {"createPauseMenu"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",
   ordinal = 3
)
   )
   public T replaceOpenToLanButton(T widget) {
      if (LockoutMatchData.isInMatch()) {
         Button button = Button.builder(Component.literal("-"), (b) -> {
         }).width(98).build();
         button.active = false;
         return (T)button;
      } else {
         return widget;
      }
   }

   @Redirect(
      method = {"createPauseMenu"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;I)Lnet/minecraft/client/gui/layouts/LayoutElement;"
)
   )
   public T removeQuitButton(GridLayout.RowHelper instance, T widget, int columnWidth) {
      if (!LockoutMatchData.isInMatch()) {
         return (T)instance.addChild(widget, columnWidth);
      } else {
         Lockout lockout = LockoutMatchData.getLockout();
         boolean buttonEnabled = Lockout.exists(lockout);
         Button.OnPress handleButtonPress = (b) -> {
            if (LockoutMatchData.isInMatch() && Lockout.exists(LockoutMatchData.getLockout())) {
               Minecraft.getInstance().setScreen(new BoardScreen());
            }

         };
         Button button;
         if (!buttonEnabled) {
            button = Button.builder(Component.literal("-"), (b) -> {
            }).width(204).build();
            button.active = false;
         } else {
            button = Button.builder(Component.literal("Open Board"), handleButtonPress).width(204).build();
         }

         return (T)instance.addChild(button, columnWidth);
      }
   }
}
