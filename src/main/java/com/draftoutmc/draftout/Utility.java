package com.draftoutmc.draftout;

import com.draftoutmc.draftout.client.LockoutBoard;
import com.draftoutmc.draftout.client.TooltipCache;
import com.draftoutmc.draftout.client.export.GoalIconExportRunner;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.client.gui.ranked.elements.ItemGridClientTooltipComponent;
import com.draftoutmc.draftout.client.gui.ranked.elements.ItemGridTooltip;
import com.draftoutmc.draftout.lockout.Goal;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.match.data.RankedData;
import com.draftoutmc.draftout.mixin.client.ConnectScreenAccessor;
import com.draftoutmc.draftout.websocket.ServerConnection;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.netty.channel.ChannelFuture;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

public class Utility {
   public static int FF000000 = -16777216;

   public static void drawBoardOverlay(GuiGraphicsExtractor graphics, LockoutBoard board, List<? extends LockoutTeam> lockoutTeams, String timer) {
      int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
      int scaleUp = guiScale == 1 ? 2 : 1;
      float textScale = guiScale == 1 ? 1.45F : 0.725F;
      LockoutConfig.BoardPosition boardPosition = LockoutConfig.getInstance().boardPosition;
      Font font = Minecraft.getInstance().font;
      int boardWidth = 4 + board.size() * 18;
      int boardHeight = 15 + board.size() * 18;
      boardWidth *= scaleUp;
      boardHeight *= scaleUp;
      int boardRightEdgeX = boardPosition == LockoutConfig.BoardPosition.LEFT ? boardWidth : graphics.guiWidth();
      int boardLeftEdgeX = boardRightEdgeX - boardWidth;
      int y = 0;
      RankedGuiUtils.blitSpriteScaled(graphics, Constants.GUI_IDENTIFIER, boardLeftEdgeX, y, boardWidth / scaleUp, boardHeight / scaleUp, (float)scaleUp);
      int x = boardLeftEdgeX + 3 * scaleUp;
      y += 3 * scaleUp;
      int startX = x;

      for(int i = 0; i < board.size(); ++i) {
         for(int j = 0; j < board.size(); ++j) {
            Goal goal = (Goal)board.getGoals().get(j + board.size() * i);
            if (goal != null) {
               if (goal.isCompleted()) {
                  graphics.fill(x, y, x + 16 * scaleUp, y + 16 * scaleUp, FF000000 | goal.getCompletedTeam().getColor());
               }

               goal.renderScaled(graphics, font, x, y, (float)scaleUp);
            }

            x += 18 * scaleUp;
         }

         y += 18 * scaleUp;
         x = startX;
      }

      RankedGuiUtils.scaledCenteredText(graphics, Component.literal(timer).withStyle(ChatFormatting.GRAY), boardLeftEdgeX + boardWidth / 2, boardHeight + 2, (float)scaleUp, -1);
      if (lockoutTeams.size() == 2) {
         LockoutTeam team1 = (LockoutTeam)lockoutTeams.getFirst();
         LockoutTeam team2 = (LockoutTeam)lockoutTeams.getLast();
         MutableComponent scoreComponent = Component.empty();

         for(LockoutTeam team : lockoutTeams) {
            scoreComponent.append(Component.literal(String.valueOf(team.getPoints(LockoutMatchData.getLockout()))).withColor(team.getColor()));
            if (!team.equals(lockoutTeams.getLast())) {
               scoreComponent.append(Component.literal("-").withStyle(ChatFormatting.GRAY));
            }
         }

         int var10000 = boardHeight - 7 * scaleUp;
         Objects.requireNonNull(font);
         int scoreY = var10000 - (int)(9.0F / 2.0F * textScale);
         int centerX = boardLeftEdgeX + boardWidth / 2;
         int scoreScreenWidth = font.width(scoreComponent) * scaleUp;
         int scoreLeft = centerX - scoreScreenWidth / 2;
         int scoreRight = centerX + scoreScreenWidth / 2;
         int namePad = 3 * scaleUp;
         int maxFontUnits1 = (int)((float)(scoreLeft - boardLeftEdgeX - namePad) / textScale) - 1;
         int maxFontUnits2 = (int)((float)(boardRightEdgeX - scoreRight - namePad) / textScale) - 1;
         Component name1 = truncatedName(font, team1.getDisplayName(), team1.getColor(), maxFontUnits1);
         Component name2 = truncatedName(font, team2.getDisplayName(), team2.getColor(), maxFontUnits2);
         float var10003 = (float)boardHeight - 6.5F * (float)scaleUp;
         Objects.requireNonNull(font);
         RankedGuiUtils.scaledCenteredText(graphics, scoreComponent, centerX, (int)(var10003 - (float)((int)(9.0F / 2.0F * (float)scaleUp))), (float)scaleUp, -1);
         RankedGuiUtils.scaledText(graphics, name1, boardLeftEdgeX + namePad, scoreY, textScale, -1);
         int name2ScreenWidth = (int)((float)font.width(name2) * textScale);
         RankedGuiUtils.scaledText(graphics, name2, boardRightEdgeX - namePad - name2ScreenWidth, scoreY, textScale, -1);
      }

   }

   private static Component truncatedName(Font font, String name, int color, int maxFontUnits) {
      if (font.width(name) <= maxFontUnits) {
         return Component.literal(name).withColor(color);
      } else {
         int ellipsisWidth = font.width("..");
         String cut = font.plainSubstrByWidth(name, maxFontUnits - ellipsisWidth);
         return Component.literal(cut + "..").withColor(color);
      }
   }

   public static void drawCenterBingoBoard(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY, LockoutBoard board) {
      int width = context.guiWidth();
      int height = context.guiHeight();
      drawCenteredBingoBoard(context, textRenderer, mouseX, mouseY, width / 2, height / 2, board);
   }

   public static void drawCenteredBingoBoard(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY, int x, int y, LockoutBoard board) {
      int boardWidth = 14 + board.size() * 18;
      int boardHeight = 14 + board.size() * 18;
      drawBingoBoardAt(context, textRenderer, mouseX, mouseY, x - boardWidth / 2, y - boardHeight / 2, board);
   }

   public static void drawBingoBoardAt(GuiGraphicsExtractor context, Font textRenderer, int mouseX, int mouseY, int boardX, int boardY, LockoutBoard board) {
      int boardWidth = 14 + board.size() * 18;
      int boardHeight = 14 + board.size() * 18;
      context.blitSprite(RenderPipelines.GUI_TEXTURED, Constants.GUI_CENTER_IDENTIFIER, boardX, boardY, boardWidth, boardHeight);
      int x = boardX + 7 + 1;
      int y = boardY + 7 + 1;
      int startX = x;
      Optional<Integer> hoveredIdx = getBoardHoveredIndex(board.size(), context.guiWidth(), context.guiHeight(), mouseX, mouseY, boardX, boardY);

      for(int i = 0; i < board.size(); ++i) {
         for(int j = 0; j < board.size(); ++j) {
            int idx = j + board.size() * i;
            Goal goal = (Goal)board.getGoals().get(idx);
            if (goal != null) {
               if (goal.isCompleted()) {
                  context.fill(x, y, x + 16, y + 16, -16777216 | goal.getCompletedTeam().getColor());
               }

               goal.render(context, textRenderer, x, y);
            }

            if (hoveredIdx.isPresent() && idx == (Integer)hoveredIdx.get()) {
               context.fill(x, y, x + 16, y + 16, -2130706433);
            }

            x += 18;
         }

         y += 18;
         x = startX;
      }

   }

   public static void drawDraftPicks(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY, LockoutBoard board, Goal leftGoal, Goal rightGoal, boolean picked) {
      int width = graphics.guiWidth();
      int height = graphics.guiHeight();
      int boardWidth = 14 + board.size() * 18;
      int x = width / 2 - boardWidth / 2;
      int boardHeight = 14 + board.size() * 18;
      int y = height / 2 - 4;
      int goalSize = 20;
      int pickSize = 14 + goalSize + 2;
      y -= pickSize + 14;
      float scale = 1.25F;
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Constants.GUI_DRAFT_PICKS, x, y, 104, 36);
      Font var10001 = Minecraft.getInstance().font;
      MutableComponent var10002 = Component.literal("or").withStyle(ChatFormatting.BOLD);
      int var10003 = width / 2;
      int var10004 = y + 18;
      Objects.requireNonNull(font);
      graphics.centeredText(var10001, var10002, var10003, var10004 - 9 / 2, -1);
      int goalX = x + 7 + 1;
      int goalY = y + 7 + 1;
      if (mouseX >= goalX && mouseX <= goalX + goalSize && mouseY >= goalY && mouseY <= goalY + goalSize) {
         graphics.fill(goalX, goalY, goalX + goalSize, goalY + goalSize, -2130706433);
         graphics.setTooltipForNextFrame(font, List.of(Component.literal(leftGoal.getGoalName()).getVisualOrderText()), mouseX, mouseY);
      }

      if (leftGoal != null) {
         leftGoal.renderScaled(graphics, font, goalX, goalY, scale);
      }

      if (picked) {
         graphics.fill(goalX, goalY, goalX + goalSize, goalY + goalSize, 1627389951);
      }

      x = width / 2 + boardWidth / 2 - pickSize;
      goalX = x + 7 + 1;
      goalY = y + 7 + 1;
      if (mouseX >= goalX && mouseX <= goalX + goalSize && mouseY >= goalY && mouseY <= goalY + goalSize) {
         graphics.fill(goalX, goalY, goalX + goalSize, goalY + goalSize, -2130706433);
         graphics.setTooltipForNextFrame(font, List.of(Component.literal(rightGoal.getGoalName()).getVisualOrderText()), mouseX, mouseY);
      }

      if (rightGoal != null) {
         rightGoal.renderScaled(graphics, font, goalX, goalY, scale);
      }

   }

   public static Optional<Integer> getBoardHoveredIndex(int size, int width, int height, int mouseX, int mouseY) {
      int x = width / 2 - (14 + size * 18) / 2;
      int y = height / 2 - (14 + size * 18) / 2;
      return getBoardHoveredIndex(size, width, height, mouseX, mouseY, x, y);
   }

   public static Optional<Integer> getDraftHoveredIdx(int width, int height, int mouseX, int mouseY) {
      int board5x5Width = 104;
      int x = width / 2 - board5x5Width / 2;
      int y = height / 2 - 4;
      int goalSize = 20;
      int pickSize = 14 + goalSize + 2;
      y -= pickSize + 14;
      int goalX = x + 7 + 1;
      int goalY = y + 7 + 1;
      if (mouseX >= goalX && mouseX <= goalX + goalSize && mouseY >= goalY && mouseY <= goalY + goalSize) {
         return Optional.of(0);
      } else {
         x = width / 2 + board5x5Width / 2 - pickSize;
         goalX = x + 7 + 1;
         goalY = y + 7 + 1;
         return mouseX >= goalX && mouseX <= goalX + goalSize && mouseY >= goalY && mouseY <= goalY + goalSize ? Optional.of(1) : Optional.empty();
      }
   }

   public static Optional<Integer> getBoardHoveredIndex(int size, int width, int height, int mouseX, int mouseY, int boardX, int boardY) {
      int x = boardX + 7 + 1;
      int y = boardY + 7 + 1;
      int startX = x;

      for(int i = 0; i < size; ++i) {
         for(int j = 0; j < size; ++j) {
            if (mouseX >= x - 1 && mouseX < x + 18 && mouseY >= y - 1 && mouseY < y + 18) {
               return Optional.of(j + i * size);
            }

            x += 18;
         }

         y += 18;
         x = startX;
      }

      return Optional.empty();
   }

   public static Goal getBoardHoveredGoal(GuiGraphicsExtractor context, int mouseX, int mouseY, LockoutBoard board) {
      Optional<Integer> hoveredIdx = getBoardHoveredIndex(board.size(), context.guiWidth(), context.guiHeight(), mouseX, mouseY);
      return (Goal)hoveredIdx.map((integer) -> (Goal)board.getGoals().get(integer)).orElse((Goal) null);
   }

   public static Goal getBoardHoveredGoal(GuiGraphicsExtractor context, int mouseX, int mouseY, LockoutBoard board, int boardX, int boardY) {
      Optional<Integer> hoveredIdx = getBoardHoveredIndex(board.size(), context.guiWidth(), context.guiHeight(), mouseX, mouseY, boardX, boardY);
      return (Goal)hoveredIdx.map((integer) -> (Goal)board.getGoals().get(integer)).orElse((Goal) null);
   }

   public static void drawGoalInformation(GuiGraphicsExtractor context, Font font, Goal goal, int mouseX, int mouseY) {
      List<ClientTooltipComponent> components = new ArrayList();
      boolean hasInfo = goal instanceof HasTooltipInfo;
      List<ItemStack> items = List.of();
      if (hasInfo) {
         for(String line : TooltipCache.getTooltip(goal.getId())) {
            components.add(ClientTooltipComponent.create(Component.literal(line).getVisualOrderText()));
         }

         items = TooltipCache.getItems(goal.getId());
      }

      if (!items.isEmpty()) {
         components.add(new ItemGridClientTooltipComponent(new ItemGridTooltip(items, 7)));
      }

      boolean hasTooltip = !items.isEmpty() || !components.isEmpty();
      String var10001 = hasTooltip ? ChatFormatting.UNDERLINE.toString() : "";
      components.addFirst(ClientTooltipComponent.create(Component.literal(var10001 + goal.getGoalName()).getVisualOrderText()));
      context.nextStratum();
      context.tooltip(font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, (Identifier)null);
   }

   public static int getBoardWidth(int size) {
      return 14 + size * 18;
   }

   public static void drawStackCount(GuiGraphicsExtractor context, int x, int y, String count) {
      Font textRenderer = Minecraft.getInstance().font;
      context.pose().pushMatrix();
      if (GoalIconExportRunner.isExportMode()) {
         context.pose().translate((float)x + 16.0F, (float)y + 9.5F);
         context.pose().scale(0.8F, 0.8F);
         context.text(textRenderer, count, -textRenderer.width(count), 0, -1, true);
      } else {
         context.pose().translate(0.0F, 0.0F);
         context.text(textRenderer, count, x + 19 - 2 - textRenderer.width(count), y + 6 + 3, -1, true);
      }

      context.pose().popMatrix();
   }

   public static String ticksToTimer(long ticks) {
      return ticksToTimer(ticks, 0);
   }

   public static String ticksToTimer(long ticks, int decimals) {
      return msToTimer(ticks * 50L, decimals);
   }

   public static String msToTimer(long timeMs, int decimals) {
      long factor = (long)Math.pow((double)10.0F, (double)(3 - decimals));
      long adjustedMs = (long)Math.floor((double)timeMs / (double)factor) * factor;
      timeMs = Math.abs(adjustedMs);
      long totalSeconds = timeMs / 1000L;
      long second = totalSeconds % 60L;
      long minute = totalSeconds / 60L % 60L;
      long hour = totalSeconds / 3600L % 24L;
      String decimalTime = "";
      if (decimals > 0) {
         long fractional = timeMs % 1000L / factor;
         decimalTime = String.format(".%0" + decimals + "d", fractional);
      }

      return hour > 0L ? String.format("%02d:%02d:%02d%s", hour, minute, second, decimalTime) : String.format("%02d:%02d%s", minute, second, decimalTime);
   }

   public static String msToSeconds(long timeMs, int decimals) {
      String decimalTime = "";
      if (decimals > 0) {
         decimalTime = String.format(".%0" + decimals + "d", (int)((double)(timeMs % 1000L) / Math.pow((double)10.0F, (double)(3 - decimals))));
      }

      return String.format("%d%s", timeMs / 1000L, decimalTime);
   }

   public static void sendScreenDataForTwitchExtension() {
      if (RankedData.linkedTwitchUsername() != null) {
         Minecraft mc = Minecraft.getInstance();
         int scaledWidth = mc.getWindow().getGuiScaledWidth();
         int scaledHeight = mc.getWindow().getGuiScaledHeight();
         int boardWidth = 90;
         int boardWidthPadded = 2 + boardWidth;
         LockoutConfig.BoardPosition boardPosition = LockoutConfig.getInstance().boardPosition;
         int x = boardPosition == LockoutConfig.BoardPosition.LEFT ? 2 : scaledWidth - boardWidthPadded;
         int y = 2;
         double xPct = (double)x / (double)scaledWidth;
         double yPct = (double)y / (double)scaledHeight;
         double widthPct = (double)boardWidth / (double)scaledWidth;
         double heightPct = (double)boardWidth / (double)scaledHeight;
         ServerConnection.getInstance().sendDisplayData(boardPosition, xPct, yPct, widthPct, heightPct);
      }
   }

   public static int brightness(int color, float amount) {
      return ARGB.color(255, (int)((float)ARGB.red(color) * amount), (int)((float)ARGB.green(color) * amount), (int)((float)ARGB.blue(color) * amount));
   }

   public static boolean isJsonNull(JsonElement element) {
      return element == null || element.isJsonNull();
   }

   public static void downloadUpdate(String downloadUrl, Consumer<Integer> onProgress) throws IOException {
      Path modsFolder = FabricLoader.getInstance().getGameDir().resolve("mods");
      String newFileName = Path.of(URI.create(downloadUrl).getPath()).getFileName().toString();
      Path tmp = modsFolder.resolve(newFileName + ".tmp");
      HttpURLConnection connection = (HttpURLConnection)URI.create(downloadUrl).toURL().openConnection();
      int fileSize = connection.getContentLength();
      int progress = 0;
      InputStream in = connection.getInputStream();

      try {
         OutputStream out = Files.newOutputStream(tmp);

         try {
            byte[] buffer = new byte[4096];
            long downloaded = 0L;

            int bytesRead;
            while((bytesRead = in.read(buffer)) != -1) {
               out.write(buffer, 0, bytesRead);
               downloaded += (long)bytesRead;
               int percent = (int)(downloaded * 100L / (long)fileSize);
               if (fileSize > 0 && percent > progress) {
                  onProgress.accept(percent);
                  progress = percent;
               }
            }
         } catch (Throwable var17) {
            if (out != null) {
               try {
                  out.close();
               } catch (Throwable var16) {
                  var17.addSuppressed(var16);
               }
            }

            throw var17;
         }

         if (out != null) {
            out.close();
         }
      } catch (Throwable var18) {
         if (in != null) {
            try {
               in.close();
            } catch (Throwable var15) {
               var18.addSuppressed(var15);
            }
         }

         throw var18;
      }

      if (in != null) {
         in.close();
      }

      Files.move(tmp, modsFolder.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
   }

   public static void deleteOldVersions() throws IOException {
      Path modsFolder = FabricLoader.getInstance().getGameDir().resolve("mods");
      String currentJarName = ((Path)((ModContainer)FabricLoader.getInstance().getModContainer("draftout").orElseThrow()).getOrigin().getPaths().getFirst()).getFileName().toString();
      Stream<Path> files = Files.list(modsFolder);

      try {
         files.filter((p) -> p.toString().endsWith(".jar")).forEach((p) -> {
            String name = p.getFileName().toString();
            if (!name.equals(currentJarName) && isDraftout(p)) {
               p.toFile().delete();
            }

         });
      } catch (Throwable var6) {
         if (files != null) {
            try {
               files.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (files != null) {
         files.close();
      }

   }

   private static boolean isDraftout(Path jarPath) {
      try {
         ZipFile zip = new ZipFile(jarPath.toFile());

         boolean var12;
         label62: {
            boolean var6;
            try {
               ZipEntry entry = zip.getEntry("fabric.mod.json");
               if (entry == null) {
                  var12 = false;
                  break label62;
               }

               InputStreamReader reader = new InputStreamReader(zip.getInputStream(entry));

               try {
                  JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                  String id = json.get("id").getAsString();
                  var6 = "draftout".equals(id) || "lockout-ranked".equals(id);
               } catch (Throwable var9) {
                  try {
                     reader.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               reader.close();
            } catch (Throwable var10) {
               try {
                  zip.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }

            zip.close();
            return var6;
         }

         zip.close();
         return var12;
      } catch (Exception var11) {
         return false;
      }
   }
   public static void abortServerConnect(ConnectScreen connectScreen) {
      ConnectScreenAccessor accessor = (ConnectScreenAccessor)connectScreen;
      synchronized(connectScreen) {
         accessor.setAborted(true);
         if (accessor.getChannelFuture() != null) {
            accessor.getChannelFuture().cancel(true);
            accessor.setChannelFuture((ChannelFuture)null);
         }

         if (accessor.getConnection() != null) {
            accessor.getConnection().disconnect(ConnectScreen.ABORT_CONNECTION);
         }

      }
   }
}
