package com.draftoutmc.draftout.lockout.goals.wear_armor;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.LockoutTeamServer;
import com.draftoutmc.draftout.Utility;
import com.draftoutmc.draftout.lockout.interfaces.HasTooltipInfo;
import com.draftoutmc.draftout.lockout.interfaces.WearArmorPieceGoal;
import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WearCarvedPumpkinFor5MinutesGoal extends WearArmorPieceGoal implements HasTooltipInfo {
   private static final List<Item> ITEMS;
   private static final int FIVE_MINUTES_TICKS = 6000;

   public WearCarvedPumpkinFor5MinutesGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Wear a Carved Pumpkin for 5 minutes";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      Player player = playerInventory.player;
      Map<UUID, Long> map = LockoutMatchData.getLockout().pumpkinWearTime;
      long wornTime = (Long)map.getOrDefault(player.getUUID(), 0L);
      ArrayList<ItemStack> armor = new ArrayList();
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.HEAD));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.CHEST));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.LEGS));
      armor.add(((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.FEET));
      boolean wearingPumpkin = false;

      for(ItemStack item : armor) {
         if (item != null && item.getItem() == Items.CARVED_PUMPKIN) {
            wearingPumpkin = true;
            break;
         }
      }

      if (wearingPumpkin) {
         ++wornTime;
         map.put(player.getUUID(), wornTime);
         if (wornTime % 20L == 0L) {
            ((LockoutTeamServer)LockoutMatchData.getLockout().getPlayerTeam(player.getUUID())).sendTooltipUpdate(this);
         }

         return wornTime >= 6000L;
      } else {
         return false;
      }
   }

   public List<String> getTooltip(LockoutTeam team, Player player) {
      List<String> tooltip = new ArrayList();
      if (player == null) {
         return tooltip;
      } else {
         long timeWorn = Math.min(6000L, (Long)LockoutMatchData.getLockout().pumpkinWearTime.getOrDefault(player.getUUID(), 0L));
         LockoutTeamServer serverTeam = (LockoutTeamServer)team;
         tooltip.add(" ");
         tooltip.add("Time worn: " + Utility.ticksToTimer(timeWorn));
         if (serverTeam.getPlayers().size() > 1) {
            tooltip.add(" ");

            for(UUID uuid : ((LockoutTeamServer)team).getPlayers()) {
               if (!Objects.equals(uuid, player.getUUID())) {
                  String var10001 = serverTeam.getPlayerName(uuid);
                  tooltip.add(var10001 + ": " + Utility.ticksToTimer(timeWorn));
               }
            }
         }

         tooltip.add(" ");
         return tooltip;
      }
   }

   static {
      ITEMS = List.of(Items.CARVED_PUMPKIN);
   }
}
