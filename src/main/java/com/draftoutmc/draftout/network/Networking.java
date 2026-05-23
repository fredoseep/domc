package com.draftoutmc.draftout.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class Networking {
   public static void registerPayloads() {
      PayloadTypeRegistry.clientboundPlay().register(UpdateTimerPayload.ID, UpdateTimerPayload.CODEC);
      PayloadTypeRegistry.clientboundPlay().register(UpdateTooltipPayload.ID, UpdateTooltipPayload.CODEC);
      PayloadTypeRegistry.clientboundPlay().register(LockoutGoalsTeamsPayload.ID, LockoutGoalsTeamsPayload.CODEC);
      PayloadTypeRegistry.clientboundPlay().register(StartLockoutPayload.ID, StartLockoutPayload.CODEC);
      PayloadTypeRegistry.clientboundPlay().register(CompleteTaskPayload.ID, CompleteTaskPayload.CODEC);
      PayloadTypeRegistry.clientboundPlay().register(EndLockoutPayload.ID, EndLockoutPayload.CODEC);
   }
}
