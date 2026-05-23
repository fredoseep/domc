package com.draftoutmc.draftout.mixin.client;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({KeyboardInput.class})
public class InputMixin extends ClientInput {
   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   public void tick(CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
         Lockout lockout = LockoutMatchData.getLockout();
         if (matchData.matchType() != MatchType.COMPETITIVE || System.currentTimeMillis() >= LockoutMatchData.CURRENT_MATCH.startingWorldGenAt()) {
            if (lockout == null) {
               KeyboardInput input = (KeyboardInput)(Object)this;
               input.keyPresses = new Input(false, false, false, false, false, input.keyPresses.shift(), false);
               this.moveVector = new Vec2(0.0F, 0.0F);
            } else {
               if (matchData.matchType() == MatchType.QUICK_PLAY && !lockout.hasStarted() || matchData.matchType() == MatchType.COMPETITIVE && !lockout.hasStarted()) {
                  KeyboardInput input = (KeyboardInput)(Object)this;
                  input.keyPresses = new Input(false, false, false, false, false, input.keyPresses.shift(), false);
                  this.moveVector = new Vec2(0.0F, 0.0F);
               }

            }
         }
      }
   }
}
