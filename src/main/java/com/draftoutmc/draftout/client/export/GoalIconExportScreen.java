package com.draftoutmc.draftout.client.export;

import com.draftoutmc.draftout.Initializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.NativeImage;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.state.WindowRenderState;
import net.minecraft.network.chat.Component;

public final class GoalIconExportScreen extends Screen {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
   private static final int GUI_ICON_SIZE = 16;
   private final int resolution = GoalIconExportEnvironment.getInt("draftout.goalIcons.resolution", "DRAFTOUT_GOAL_ICON_RESOLUTION", 256);
   private final String version = GoalIconExportEnvironment.getString("draftout.goalIcons.version", "DRAFTOUT_GOAL_ICON_VERSION", defaultVersion());
   private final Path outputRoot;
   private final Path assetRoot;
   private final Path manifestFile;
   private final List<GoalIconManifestEntry> manifestEntries;
   private final List<GoalIconExportEntry> entries;
   private final List<BufferedImage> capturedFrames = new ArrayList();
   private final List<Integer> capturedDelays = new ArrayList();
   private int entryIndex;
   private int frameIndex;
   private boolean renderedCurrent;
   private boolean awaitingScreenshot;

   public GoalIconExportScreen() {
      super(Component.literal("Draftout Goal Icon Export"));
      this.outputRoot = Path.of(GoalIconExportEnvironment.getString("draftout.goalIcons.outputDir", "DRAFTOUT_GOAL_ICON_OUTPUT_DIR", Minecraft.getInstance().gameDirectory.toPath().resolve("goal-icon-export").toString()));
      this.assetRoot = this.outputRoot.resolve("generated-goal-icons");
      this.manifestFile = this.outputRoot.resolve("goal-icon-manifest.json");
      this.prepareOutputDirectory();
      GoalIconExportPlan plan = GoalIconExportPlan.build(this.assetRoot, GoalIconExportEnvironment.getInt("draftout.goalIcons.limit", "DRAFTOUT_GOAL_ICON_LIMIT", 0));
      this.manifestEntries = plan.manifestEntries();
      this.entries = plan.entries();
      int renderFrameCount = this.entries.stream().mapToInt((entry) -> entry.renderFrames().size()).sum();
      System.out.println("[DraftoutGoalIconExport] prepared " + this.manifestEntries.size() + " entries, " + renderFrameCount + " render frames, and " + this.entries.size() + " WebP targets");
   }

   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
      super.extractRenderState(graphics, mouseX, mouseY, partialTick);
      WindowRenderState windowState = Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState;
      windowState.guiScale = Math.max(1, this.resolution / 16);
      graphics.fill(0, 0, 16, 16, -16711165);
      if (this.entryIndex < this.entries.size()) {
         GoalIconExportFrame renderFrame = this.currentRenderFrame();
         GoalIconAtlasAnimationController.setAnimationTick(renderFrame.animationTick());
         renderFrame.renderer().render(graphics, Minecraft.getInstance().font, 0, 0);
         if (!this.renderedCurrent) {
            this.renderedCurrent = true;
         }
      }

   }

   protected void extractBlurredBackground(GuiGraphicsExtractor context) {
   }

   public void tick() {
      if (this.entryIndex < this.entries.size()) {
         if (this.renderedCurrent && !this.awaitingScreenshot) {
            GoalIconExportEntry entry = this.currentEntry();
            GoalIconExportFrame renderFrame = this.currentRenderFrame();
            this.awaitingScreenshot = true;
            Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget(), (image) -> {
               label96: {
                  try {
                     NativeImage twrVar0$ = image;

                     try {
                        GoalIconCapturedImage captured = GoalIconExportFiles.captureIconRegion(image, this.resolution, this.resolution);
                        GoalIconImageStats stats = captured.stats();
                        if (stats.nonTransparentPixels() == 0) {
                           PrintStream var10000 = System.err;
                           String var10001 = entry.webPath();
                           var10000.println("[DraftoutGoalIconExport] warning: rendered blank frame for " + var10001 + " frame " + (this.frameIndex + 1));
                        }

                        this.capturedFrames.add(captured.image());
                        this.capturedDelays.add(renderFrame.delayMs());
                        int var16 = this.entryIndex + 1;
                        System.out.println("[DraftoutGoalIconExport] captured entry " + var16 + "/" + this.entries.size() + " frame " + (this.frameIndex + 1) + "/" + entry.renderFrames().size() + " " + entry.webPath() + " pixels=" + stats.nonTransparentPixels() + " bounds=" + stats.bounds());
                     } catch (Throwable var13) {
                        if (image != null) {
                           try {
                              twrVar0$.close();
                           } catch (Throwable x2) {
                              var13.addSuppressed(x2);
                           }
                        }

                        throw var13;
                     }

                     if (image != null) {
                        image.close();
                     }
                     break label96;
                  } catch (Exception e) {
                     this.fail(e);
                  } finally {
                     this.awaitingScreenshot = false;
                  }

                  return;
               }

               ++this.frameIndex;
               this.renderedCurrent = false;
               if (this.frameIndex >= entry.renderFrames().size()) {
                  this.writeCurrentEntry();
                  ++this.entryIndex;
                  this.frameIndex = 0;
                  this.capturedFrames.clear();
                  this.capturedDelays.clear();
                  if (this.entryIndex >= this.entries.size()) {
                     this.finish();
                  }
               }

            });
         }
      }
   }

   private GoalIconExportEntry currentEntry() {
      return (GoalIconExportEntry)this.entries.get(this.entryIndex);
   }

   private GoalIconExportFrame currentRenderFrame() {
      return (GoalIconExportFrame)this.currentEntry().renderFrames().get(this.frameIndex);
   }

   private void writeCurrentEntry() {
      GoalIconExportEntry entry = this.currentEntry();

      try {
         GoalIconExportFiles.writeWebp(entry.file(), this.capturedFrames, this.capturedDelays);
         int var10001 = this.entryIndex + 1;
         System.out.println("[DraftoutGoalIconExport] wrote WebP entry " + var10001 + "/" + this.entries.size() + " " + entry.webPath() + " frames=" + this.capturedFrames.size() + " bytes=" + entry.file().toFile().length());
      } catch (Exception e) {
         this.fail(e);
      }

   }

   private void finish() {
      try {
         GoalIconRuntimeManifest manifest = new GoalIconRuntimeManifest(this.version, this.resolution, this.manifestEntries);
         GoalIconExportFiles.writeString(this.manifestFile, GSON.toJson(manifest) + "\n");
         PrintStream var10000 = System.out;
         int var10001 = this.entries.size();
         var10000.println("[DraftoutGoalIconExport] finished " + var10001 + " WebPs in " + String.valueOf(this.outputRoot.toAbsolutePath()));
         Runtime.getRuntime().halt(0);
      } catch (Exception e) {
         this.fail(e);
      }

   }

   private void fail(Throwable throwable) {
      System.err.println("[DraftoutGoalIconExport] failed");
      throwable.printStackTrace(System.err);
      Runtime.getRuntime().halt(1);
   }

   private void prepareOutputDirectory() {
      try {
         GoalIconExportFiles.prepareOutputDirectory(this.outputRoot, this.assetRoot);
      } catch (IOException e) {
         throw new RuntimeException("Could not prepare goal icon output directory", e);
      }
   }

   private static String defaultVersion() {
      return Initializer.MOD_VERSION == null ? "draftout-dev" : "draftout-" + Initializer.MOD_VERSION.getFriendlyString();
   }
}
