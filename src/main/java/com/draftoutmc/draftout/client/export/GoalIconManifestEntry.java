package com.draftoutmc.draftout.client.export;

import com.draftoutmc.draftout.lockout.icon.GoalIconEntry;

record GoalIconManifestEntry(String key, String goalId, String data, String label, String path, boolean enchanted) {
   static GoalIconManifestEntry from(String key, GoalIconEntry entry, String path, boolean enchanted) {
      return new GoalIconManifestEntry(key, entry.goalId(), entry.data(), entry.label(), path, enchanted);
   }
}
