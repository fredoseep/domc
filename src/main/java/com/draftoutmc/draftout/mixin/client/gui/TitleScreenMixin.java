package com.draftoutmc.draftout.mixin.client.gui;

import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutMainScreen;
import com.draftoutmc.draftout.client.gui.ranked.screen.LockoutWaitingScreen;
import com.draftoutmc.draftout.websocket.ServerConnection;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({TitleScreen.class})
public class TitleScreenMixin {
   @Inject(
      method = {"createNormalMenuOptions"},
      at = {@At("HEAD")}
   )
   public void addConnectButton(int topPos, int spacing, CallbackInfoReturnable<Integer> cir) {
      TitleScreen titleScreen = (TitleScreen)(Object)this;
      Minecraft minecraft = Minecraft.getInstance();
      SpriteIconButton connect = (SpriteIconButton)titleScreen.addRenderableWidget(SpriteIconButton.builder(Component.literal("Connect"), (button) -> {
         boolean rightCtrlHeld = InputConstants.isKeyDown(minecraft.getWindow(), 345);
         boolean rightShiftHeld = InputConstants.isKeyDown(minecraft.getWindow(), 344);
         boolean connected = ServerConnection.connected();
         if (!rightCtrlHeld) {
            if (connected) {
               Minecraft.getInstance().setScreen(new LockoutMainScreen());
            } else {
               ServerConnection.setType(ServerConnection.Type.SERVER);
               minecraft.setScreen(new LockoutWaitingScreen("Connecting to server..."));
               ServerConnection sc = ServerConnection.getInstance();
               sc.connectWithTimeout();
            }
         } else if (!connected) {
            ServerConnection.setType(ServerConnection.Type.LOCALHOST);
            minecraft.setScreen(new LockoutWaitingScreen("Connecting to server..."));
            ServerConnection sc = ServerConnection.getInstance();
            sc.connectWithTimeout();
         } else {
            if (rightCtrlHeld && rightShiftHeld) {
               ServerConnection.getInstance().close();
            }

         }
      }, true).width(20).sprite(Identifier.fromNamespaceAndPath("draftout", "recovery_compass_16"), 15, 15).build());
      connect.setPosition(titleScreen.width / 2 + 100 + 2, topPos);
      connect.setTooltip(Tooltip.create(Component.literal("Draftout")));
   }
}
