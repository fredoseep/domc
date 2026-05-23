package com.draftoutmc.draftout.network;

import com.draftoutmc.draftout.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CompleteTaskPayload(String goal, int teamIndex) implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<CompleteTaskPayload> ID;
   public static final StreamCodec<RegistryFriendlyByteBuf, CompleteTaskPayload> CODEC;

   public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
      return ID;
   }

   static {
      ID = new CustomPacketPayload.Type(Constants.COMPLETE_TASK_PACKET);
      CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, CompleteTaskPayload::goal, ByteBufCodecs.INT, CompleteTaskPayload::teamIndex, CompleteTaskPayload::new);
   }
}
