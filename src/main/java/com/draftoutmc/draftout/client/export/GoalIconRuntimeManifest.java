package com.draftoutmc.draftout.client.export;

import java.util.List;

record GoalIconRuntimeManifest(String version, int resolution, List<GoalIconManifestEntry> entries) {
   GoalIconRuntimeManifest(String version, int resolution, List<GoalIconManifestEntry> entries) {
      entries = List.copyOf(entries);
      this.version = version;
      this.resolution = resolution;
      this.entries = entries;
   }
}
