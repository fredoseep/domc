package com.draftoutmc.draftout.client.export;

import java.nio.file.Path;
import java.util.List;

record GoalIconExportEntry(String key, String goalId, String data, Path file, String webPath, List<GoalIconExportFrame> renderFrames) {
   GoalIconExportEntry(String key, String goalId, String data, Path file, String webPath, List<GoalIconExportFrame> renderFrames) {
      renderFrames = List.copyOf(renderFrames);
      this.key = key;
      this.goalId = goalId;
      this.data = data;
      this.file = file;
      this.webPath = webPath;
      this.renderFrames = renderFrames;
   }
}
