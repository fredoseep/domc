package com.draftoutmc.draftout.client;

import com.draftoutmc.draftout.network.UpdateTooltipPayload;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.ItemStack;

public class TooltipCache {
   private static final Map<String, String> TOOLTIPS = new HashMap();
   private static final Map<String, List<ItemStack>> ITEMS = new HashMap();

   public static void receive(UpdateTooltipPayload payload) {
      TOOLTIPS.put(payload.goal(), payload.tooltip());
      ITEMS.put(payload.goal(), payload.items());
   }

   public static List<String> getTooltip(String goalId) {
      String raw = (String)TOOLTIPS.get(goalId);
      return raw != null && !raw.isBlank() ? Arrays.asList(raw.split("\n")) : Collections.emptyList();
   }

   public static List<ItemStack> getItems(String goalId) {
      return (List)ITEMS.getOrDefault(goalId, Collections.emptyList());
   }
}
