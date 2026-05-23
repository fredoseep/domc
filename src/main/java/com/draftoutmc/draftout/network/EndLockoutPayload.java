package com.draftoutmc.draftout.network;

import com.draftoutmc.draftout.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record EndLockoutPayload(int[] winners, long time) implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<EndLockoutPayload> ID;
   public static final StreamCodec<RegistryFriendlyByteBuf, EndLockoutPayload> CODEC;

   public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
      return ID;
   }

   static {
      ID = new CustomPacketPayload.Type(Constants.END_LOCKOUT_PACKET);
      CODEC = StreamCodec.composite(StreamCodec.ofMember((winners, buf) -> buf.writeVarIntArray(winners), FriendlyByteBuf::readVarIntArray), EndLockoutPayload::winners, ByteBufCodecs.LONG, EndLockoutPayload::time, EndLockoutPayload::new);
   }
}
