package com.draftoutmc.draftout.mixin.client.gui;

import com.draftoutmc.draftout.Initializer;
import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.RankedRank;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PlayerTabOverlay.class})
public class PlayerTabOverlayMixin {
   @Shadow
   private @Nullable Component footer;
   @Shadow
   private @Nullable Component header;
   @Unique
   private static Identifier GRASS_SIDE = Identifier.fromNamespaceAndPath("draftout", "tab/overworld");
   @Unique
   private static Identifier NETHERRACK = Identifier.fromNamespaceAndPath("draftout", "tab/nether");
   @Unique
   private static Identifier ENDSTONE = Identifier.fromNamespaceAndPath("draftout", "tab/end");

   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")}
   )
   public void init(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         String version = Initializer.MOD_VERSION.getFriendlyString();
         this.header = Component.literal(" ".repeat(32) + "\n").append(Component.literal(String.format("Draftout v%s\n", version)).withStyle(ChatFormatting.BOLD).withColor(RankedRank.DIAMOND_1.getColor())).append(Component.literal(String.format("Mode: %s", matchData.matchType().getDisplayName()))).append(Component.literal("\n"));
         String score = "";
         String time = "00:00";
         if (LockoutMatchData.getLockout() != null) {
            Lockout lockout = LockoutMatchData.getLockout();
            time = Utility.msToTimer(lockout.getTimer(), 0);
         }

         this.footer = Component.literal("\n").append(Component.literal(String.format(score))).append(Component.literal(String.format(time))).append(Component.literal("\n"));
      }
   }

   @Redirect(
      method = {"extractRenderState"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getPlayerInfos()Ljava/util/List;"
)
   )
   public List<PlayerInfo> getPlayerInfos(PlayerTabOverlay instance) {
      return !LockoutMatchData.isInMatch() ? instance.getPlayerInfos() : LockoutMatchData.CURRENT_MATCH.playerInfos();
   }

   @Redirect(
      method = {"extractRenderState"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"
)
   )
   public Component getPlayerInfos(PlayerTabOverlay instance, PlayerInfo info) {
      if (!LockoutMatchData.isInMatch()) {
         return instance.getNameForDisplay(info);
      } else {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(info.getProfile().id())).findFirst();
         if (playerOptional.isEmpty()) {
            return instance.getNameForDisplay(info);
         } else {
            LockoutMatchData.LockoutMatchPlayer player = (LockoutMatchData.LockoutMatchPlayer)playerOptional.get();
            return Component.literal(player.username());
         }
      }
   }

   @ModifyVariable(
      method = {"extractRenderState"},
      at = @At("STORE"),
      ordinal = 8
   )
   private int widenSlotForDimensionIcon(int slotWidth) {
      return !LockoutMatchData.isInMatch() ? slotWidth : slotWidth + 9;
   }

   @ModifyVariable(
      method = {"extractRenderState"},
      at = @At("STORE"),
      name = {"xxo"}
   )
   private int shiftTablistRight(int xxo) {
      return !LockoutMatchData.isInMatch() ? xxo : xxo + 4;
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/components/PlayerFaceExtractor;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;IIIZZI)V",
   shift = Shift.BEFORE
)}
   )
   private void drawCustomIcon(GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, @Nullable Objective displayObjective, CallbackInfo ci, @Local(name = {"playerInfos"}) List<PlayerInfo> playerInfos, @Local(name = {"i"}) int i, @Local(name = {"xo"}) int xo, @Local(name = {"yo"}) int yo) {
      if (LockoutMatchData.isInMatch()) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         Optional<LockoutMatchData.LockoutMatchPlayer> playerOptional = matchData.players().stream().filter((lmp) -> lmp.uuid().equals(((PlayerInfo)playerInfos.get(i)).getProfile().id())).findFirst();
         if (!playerOptional.isEmpty()) {
            LockoutMatchData.LockoutMatchPlayer player = (LockoutMatchData.LockoutMatchPlayer)playerOptional.get();
            Identifier var10000;
            switch (player.dimension()) {
               case "overworld" -> var10000 = GRASS_SIDE;
               case "nether" -> var10000 = NETHERRACK;
               case "end" -> var10000 = ENDSTONE;
               default -> var10000 = GRASS_SIDE;
            }

            Identifier icon = var10000;
            RankedGuiUtils.blitSpriteScaled(graphics, icon, xo - 9, yo, 16, 16, 0.5F);
         }
      }
   }
}
