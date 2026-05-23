package com.draftoutmc.draftout.network;

import com.draftoutmc.draftout.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateTimerPayload(long ticks) implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<UpdateTimerPayload> ID;
   public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTimerPayload> CODEC;

   public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
      return ID;
   }

   static {
      ID = new CustomPacketPayload.Type(Constants.UPDATE_TIMER_PACKET);
      CODEC = StreamCodec.composite(ByteBufCodecs.LONG, UpdateTimerPayload::ticks, UpdateTimerPayload::new);
   }
}
