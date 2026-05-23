package com.draftoutmc.draftout.client.gui.ranked.elements;

import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.client.gui.ranked.RankedGuiUtils;
import com.draftoutmc.draftout.match.MatchType;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.websocket.ServerConnection;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class QueueInfoRenderer {
   private static ItemStack COMPASS = null;
   private static ItemStack RECOVERY_COMPASS = null;

   public static void render(GuiGraphicsExtractor graphics) {
      if (!LockoutMatchData.isInMatch() || !LockoutMatchData.CURRENT_MATCH.gotGameData()) {
         Identifier skin = null;
         ItemStack item = null;
         boolean isSkin = false;
         MatchType queueType = ServerConnection.connected() ? ServerConnection.getInstance().queueType : null;
         Font font = Minecraft.getInstance().font;
         Component text;
         if (LockoutMatchData.isInMatch()) {
            LockoutMatchData matchData = LockoutMatchData.CURRENT_MATCH;
            Optional<LockoutMatchData.LockoutMatchPlayer> opp = matchData.players().stream().filter((lmp) -> !lmp.uuid().equals(Minecraft.getInstance().getUser().getProfileId())).findFirst();
            skin = ((LockoutMatchData.LockoutMatchPlayer)opp.get()).skin();
            isSkin = true;
            text = Component.empty().append(Component.literal(((LockoutMatchData.LockoutMatchPlayer)opp.get()).username()).append(" ")).append(RankedGuiUtils.getRankNameAndEloComponent(matchData.matchType(), ((LockoutMatchData.LockoutMatchPlayer)opp.get()).ranked(), ((LockoutMatchData.LockoutMatchPlayer)opp.get()).elo()));
         } else {
            if (!ServerConnection.connected() || queueType == null) {
               return;
            }

            if (RECOVERY_COMPASS == null) {
               RECOVERY_COMPASS = Items.RECOVERY_COMPASS.getDefaultInstance();
               COMPASS = Items.COMPASS.getDefaultInstance();
            }

            item = queueType == MatchType.COMPETITIVE ? RECOVERY_COMPASS : COMPASS;
            long time = System.currentTimeMillis() - ServerConnection.getInstance().queueStartTimeMs;
            text = Component.empty().append(Component.literal("In queue...")).append(Component.literal(String.format(" (%s)", Utility.msToTimer(time, 0))));
         }

         int x = 4;
         int y = 4;
         int spriteSize = 16;
         int gap = 4;
         if (isSkin) {
            graphics.outline(x, y, spriteSize, spriteSize, -1);
            RankedGuiUtils.renderHead(graphics, skin, x + 1, y + 1, spriteSize - 2);
         } else {
            graphics.item(item, x, y);
         }

         int var10003 = x + spriteSize + gap;
         int var10004 = y + 8;
         Objects.requireNonNull(font);
         graphics.text(font, text, var10003, var10004 - 9 / 2, -1);
      }
   }
}
