package com.draftoutmc.draftout.network;

import com.draftoutmc.draftout.Constants;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record UpdateTooltipPayload(String goal, String tooltip, List<ItemStack> items) implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<UpdateTooltipPayload> ID;
   public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTooltipPayload> CODEC;

   public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
      return ID;
   }

   static {
      ID = new CustomPacketPayload.Type(Constants.UPDATE_TOOLTIP);
      CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, UpdateTooltipPayload::goal, ByteBufCodecs.STRING_UTF8, UpdateTooltipPayload::tooltip, ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), UpdateTooltipPayload::items, UpdateTooltipPayload::new);
   }
}
