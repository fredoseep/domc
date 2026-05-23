package com.draftoutmc.draftout.client.export;

import com.mojang.blaze3d.platform.NativeImage;
import dev.matrixlab.webp4j.WebPCodec;
import dev.matrixlab.webp4j.gif.GifToWebPConfig;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;

final class GoalIconExportFiles {
   static final int TRANSPARENCY_KEY = -16711165;

   private GoalIconExportFiles() {
   }

   static void prepareOutputDirectory(Path outputRoot, Path assetRoot) throws IOException {
      deleteRecursively(assetRoot);
      Files.createDirectories(outputRoot);
      Files.createDirectories(assetRoot);
   }

   static GoalIconCapturedImage captureIconRegion(NativeImage nativeImage, int width, int height) {
      int copyWidth = Math.min(width, nativeImage.getWidth());
      int copyHeight = Math.min(height, nativeImage.getHeight());
      int nonTransparent = 0;
      int minX = width;
      int minY = height;
      int maxX = -1;
      int maxY = -1;
      BufferedImage bufferedImage = new BufferedImage(width, height, 2);

      for(int x = 0; x < copyWidth; ++x) {
         for(int y = 0; y < copyHeight; ++y) {
            int pixel = nativeImage.getPixel(x, y);
            int outPixel = pixel == -16711165 ? 0 : pixel;
            if (outPixel != 0) {
               ++nonTransparent;
               minX = Math.min(minX, x);
               minY = Math.min(minY, y);
               maxX = Math.max(maxX, x);
               maxY = Math.max(maxY, y);
            }

            bufferedImage.setRGB(x, y, outPixel);
         }
      }

      return new GoalIconCapturedImage(bufferedImage, new GoalIconImageStats(nonTransparent, minX, minY, maxX, maxY));
   }

   static void writeWebp(Path file, List<BufferedImage> frames, List<Integer> delaysMs) throws IOException {
      Files.createDirectories(file.getParent());
      if (frames.isEmpty()) {
         throw new IOException("Cannot write WebP with no frames: " + String.valueOf(file));
      } else if (frames.size() == 1) {
         Files.write(file, WebPCodec.encodeLosslessImage((BufferedImage)frames.getFirst()), new OpenOption[0]);
      } else if (frames.size() != delaysMs.size()) {
         throw new IOException("WebP frame/delay count mismatch for " + String.valueOf(file));
      } else {
         int[] delays = delaysMs.stream().mapToInt(Integer::intValue).toArray();
         GifToWebPConfig config = GifToWebPConfig.createLosslessConfig().setLoopCount(0).setCompressionMethod(6).setMinimizeSize(true);
         Files.write(file, WebPCodec.createAnimatedWebP(frames, delays, config), new OpenOption[0]);
      }
   }

   static void writeString(Path file, String contents) throws IOException {
      Files.createDirectories(file.getParent());
      Files.writeString(file, contents, StandardCharsets.UTF_8);
   }

   private static void deleteRecursively(Path path) throws IOException {
      if (Files.exists(path, new LinkOption[0])) {
         if (Files.isDirectory(path, new LinkOption[0])) {
            DirectoryStream<Path> children = Files.newDirectoryStream(path);

            try {
               for(Path child : children) {
                  deleteRecursively(child);
               }
            } catch (Throwable var5) {
               if (children != null) {
                  try {
                     children.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (children != null) {
               children.close();
            }
         }

         Files.delete(path);
      }
   }
}
