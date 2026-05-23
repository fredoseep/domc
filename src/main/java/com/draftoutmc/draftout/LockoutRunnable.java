package com.draftoutmc.draftout;

import com.draftoutmc.draftout.server.LockoutServer;

@FunctionalInterface
public interface LockoutRunnable {
   default void runTaskAt(long ms) {
      if (ms <= System.currentTimeMillis()) {
         this.run();
      } else {
         LockoutServer.RUNNABLES.put(this, ms);
      }
   }

   void run();
}
