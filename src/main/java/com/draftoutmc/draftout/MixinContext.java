package com.draftoutmc.draftout;

import net.minecraft.server.level.ServerPlayer;

public class MixinContext {
   public static final ThreadLocal<ServerPlayer> BLOCK_PLACING_PLAYER = new ThreadLocal();
   public static final ThreadLocal<ServerPlayer> PUMPKIN_CARVING_PLAYER = new ThreadLocal();
}
