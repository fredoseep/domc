package com.draftoutmc.draftout.network;

import com.draftoutmc.draftout.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class StartLockoutPayload implements CustomPacketPayload {
   public static final StartLockoutPayload INSTANCE = new StartLockoutPayload();
   public static final CustomPacketPayload.Type<StartLockoutPayload> ID;
   public static final StreamCodec<RegistryFriendlyByteBuf, StartLockoutPayload> CODEC;

   private StartLockoutPayload() {
   }

   public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
      return ID;
   }

   static {
      ID = new CustomPacketPayload.Type(Constants.START_LOCKOUT_PACKET);
      CODEC = StreamCodec.unit(INSTANCE);
   }
}
