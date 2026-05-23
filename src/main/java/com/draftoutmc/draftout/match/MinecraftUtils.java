package com.draftoutmc.draftout.match;

import com.draftoutmc.draftout.Lockout;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class MinecraftUtils {
   private static final Queue<Runnable> minecraftRunnablesQueue = new ConcurrentLinkedQueue();
   private static volatile boolean isDisconnecting = false;

   public static void notMinecraft$disconnectFromWorld() {
      onMinecraftDisconnectStart();
      Minecraft.getInstance().execute(() -> Minecraft.getInstance().disconnectFromWorld(Component.literal("Loading...")));
   }

   public static void notMinecraft$execute(Runnable runnable) {
      minecraftRunnablesQueue.add(runnable);
      if (!isDisconnecting) {
         Minecraft.getInstance().execute(MinecraftUtils::drainQueue);
      }

   }

   public static void onMinecraftDisconnectStart() {
      isDisconnecting = true;
   }

   public static void onMinecraftDisconnectEnd() {
      isDisconnecting = false;
      drainQueue();
   }

   private static void drainQueue() {
      Runnable task;
      while((task = (Runnable)minecraftRunnablesQueue.poll()) != null) {
         try {
            task.run();
         } catch (Exception e) {
            Lockout.error("Error in task", e);
         }
      }

   }
}
