package com.draftoutmc.draftout.mixin.client;

import io.netty.channel.ChannelFuture;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.network.Connection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ConnectScreen.class})
public interface ConnectScreenAccessor {
    @Accessor
    void setAborted(boolean var1);

    @Accessor
    @Nullable ChannelFuture getChannelFuture();

    @Accessor
    void setChannelFuture(@Nullable ChannelFuture var1);

    @Accessor
    @Nullable Connection getConnection();
}
