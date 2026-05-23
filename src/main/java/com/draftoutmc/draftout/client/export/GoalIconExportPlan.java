package com.draftoutmc.draftout.client.export;

import com.draftoutmc.draftout.lockout.icon.GoalIconAnimationMode;
import com.draftoutmc.draftout.lockout.icon.GoalIconCatalog;
import com.draftoutmc.draftout.lockout.icon.GoalIconEntry;
import com.draftoutmc.draftout.lockout.icon.GoalIconFrame;
import com.draftoutmc.draftout.lockout.icon.GoalIconRenderer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

record GoalIconExportPlan(List<GoalIconManifestEntry> manifestEntries, List<GoalIconExportEntry> entries) {
   private static final int DEFAULT_CYCLE_MS = 3000;

   GoalIconExportPlan(List<GoalIconManifestEntry> manifestEntries, List<GoalIconExportEntry> entries) {
      manifestEntries = List.copyOf(manifestEntries);
      entries = List.copyOf(entries);
      this.manifestEntries = manifestEntries;
      this.entries = entries;
   }

   static GoalIconExportPlan build(Path assetRoot, int limit) {
      List<GoalIconManifestEntry> manifestEntries = new ArrayList();
      List<GoalIconExportEntry> exportEntries = new ArrayList();
      Set<String> keys = new HashSet();

      for(GoalIconEntry entry : GoalIconCatalog.collectEntries()) {
         String key = goalKey(entry.goalId(), entry.data());
         if (!keys.add(key)) {
            throw new IllegalStateException("Duplicate goal icon manifest key " + key);
         }

         String goalSlug = slug(entry.goalId());
         String dataSlug = dataSlug(entry.data());
         Path goalDir = assetRoot.resolve(goalSlug).resolve(dataSlug);
         String webPath = "/generated-goal-icons/" + goalSlug + "/" + dataSlug + "/icon.webp";
         boolean enchanted = entryEnchanted(entry);
         List<GoalIconExportFrame> renderFrames = renderFramesFor(entry, key);
         manifestEntries.add(GoalIconManifestEntry.from(key, entry, webPath, enchanted));
         exportEntries.add(new GoalIconExportEntry(key, entry.goalId(), entry.data(), goalDir.resolve("icon.webp"), webPath, renderFrames));
         if (limit > 0 && manifestEntries.size() >= limit) {
            break;
         }
      }

      return new GoalIconExportPlan(manifestEntries, exportEntries);
   }

   private static boolean entryEnchanted(GoalIconEntry entry) {
      Boolean enchanted = null;

      for(GoalIconFrame frame : entry.frames()) {
         if (enchanted == null) {
            enchanted = frame.enchanted();
         } else if (enchanted != frame.enchanted()) {
            String var10002 = entry.goalId();
            throw new IllegalStateException("Mixed enchanted frames cannot be represented by one icon: " + var10002 + "::" + entry.data());
         }
      }

      return Boolean.TRUE.equals(enchanted);
   }

   private static List<GoalIconExportFrame> renderFramesFor(GoalIconEntry entry, String key) {
      List<GoalIconFrame> frames = orderedFrames(entry, key);
      if (frames.size() == 1) {
         GoalIconFrame frame = (GoalIconFrame)frames.getFirst();
         int subFrameDelay = frame.subFrameCycleMs() == null ? 3000 : frame.subFrameCycleMs();
         List<GoalIconRenderer> renderers = frame.subFrameRenderers();
         return IntStream.range(0, renderers.size()).mapToObj((i) -> new GoalIconExportFrame((GoalIconRenderer)renderers.get(i), subFrameDelay, i)).toList();
      } else {
         int topFrameDelay = entry.cycleMs() == null ? 3000 : entry.cycleMs();
         List<GoalIconExportFrame> renderFrames = new ArrayList();

         for(GoalIconFrame frame : frames) {
            List<GoalIconRenderer> renderers = frame.subFrameRenderers();
            if (renderers.size() == 1) {
               renderFrames.add(new GoalIconExportFrame((GoalIconRenderer)renderers.getFirst(), topFrameDelay, 0));
            } else {
               int subFrameDelay = frame.subFrameCycleMs() == null ? topFrameDelay : frame.subFrameCycleMs();
               int elapsed = 0;

               for(int index = 0; elapsed < topFrameDelay; ++index) {
                  renderFrames.add(new GoalIconExportFrame((GoalIconRenderer)renderers.get(index % renderers.size()), Math.min(subFrameDelay, topFrameDelay - elapsed), index));
                  elapsed += subFrameDelay;
               }
            }
         }

         return renderFrames;
      }
   }

   private static List<GoalIconFrame> orderedFrames(GoalIconEntry entry, String key) {
      if (entry.animationMode() == GoalIconAnimationMode.SLOT_GROUP_RANDOM && entry.frames().size() % 4 == 0) {
         int groupSize = entry.frames().size() / 4;
         Random random = new Random((long)key.hashCode());
         List<List<GoalIconFrame>> groups = new ArrayList();

         for(int group = 0; group < 4; ++group) {
            List<GoalIconFrame> slotFrames = new ArrayList(entry.frames().subList(group * groupSize, (group + 1) * groupSize));
            Collections.shuffle(slotFrames, random);
            groups.add(slotFrames);
         }

         List<GoalIconFrame> interleaved = new ArrayList(entry.frames().size());

         for(int offset = 0; offset < groupSize; ++offset) {
            for(List<GoalIconFrame> group : groups) {
               interleaved.add((GoalIconFrame)group.get(offset));
            }
         }

         return interleaved;
      } else {
         return entry.frames();
      }
   }

   private static String goalKey(String goalId, String data) {
      String var10000 = goalId.trim().toUpperCase();
      return var10000 + "::" + data;
   }

   private static String dataSlug(String data) {
      return data != null && !data.equals("null") ? slug(data.replace("&", "__")) : "null";
   }

   private static String slug(String value) {
      String slug = value.toLowerCase().replaceAll("[^a-z0-9._-]+", "-").replaceAll("-+", "-").replaceAll("^-|-$", "");
      return slug.isEmpty() ? "value" : slug;
   }
}
