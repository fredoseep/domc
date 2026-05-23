package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.SeedCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SeedCommand.class})
public class SeedCommandMixin {
   @Inject(
      method = {"register"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void dontRegisterSeedCommand(CommandDispatcher<CommandSourceStack> dispatcher, boolean checkPermissions, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         ci.cancel();
      }

   }
}
