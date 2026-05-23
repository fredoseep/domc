package com.draftoutmc.draftout.lockout.interfaces;

import com.draftoutmc.draftout.LockoutTeam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface HasTooltipInfo {
   int MAX_LINE_SIZE = 45;

   List<String> getTooltip(LockoutTeam var1, Player var2);

   default List<ItemStack> getItems(LockoutTeam team, Player player) {
      return Collections.emptyList();
   }

   static List<String> commaSeparatedList(List<String> values) {
      List<String> lines = new ArrayList();
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < values.size(); ++i) {
         String val = (String)values.get(i);
         boolean isLast = i + 1 == values.size();
         String[] words = val.split(" ");

         for(int j = 0; j < words.length; ++j) {
            String word = words[j];
            boolean isLastWord = j + 1 == words.length;
            boolean canFit = sb.length() + word.length() + (isLastWord && !isLast ? 1 : 0) <= 45;
            if (!canFit) {
               String var10001 = String.valueOf(ChatFormatting.GRAY);
               lines.add(var10001 + " " + String.valueOf(ChatFormatting.ITALIC) + sb.toString().trim());
               sb.setLength(0);
            }

            sb.append(word);
            if (!isLast && isLastWord) {
               sb.append(",");
            }

            sb.append(" ");
         }
      }

      if (!sb.isEmpty()) {
         String var11 = String.valueOf(ChatFormatting.GRAY);
         lines.add(var11 + " " + String.valueOf(ChatFormatting.ITALIC) + sb.toString().trim());
      }

      return lines;
   }
}
