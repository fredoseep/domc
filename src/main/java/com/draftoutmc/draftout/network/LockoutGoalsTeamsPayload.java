package com.draftoutmc.draftout.network;

import com.draftoutmc.draftout.Constants;
import com.draftoutmc.draftout.LockoutTeam;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import oshi.util.tuples.Pair;

public record LockoutGoalsTeamsPayload(List<LockoutTeam> teams, List<Pair<Pair<String, String>, Integer>> goals, boolean isRunning) implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<LockoutGoalsTeamsPayload> ID;
   public static final StreamCodec<RegistryFriendlyByteBuf, LockoutGoalsTeamsPayload> CODEC;

   public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
      return ID;
   }

   static {
      ID = new CustomPacketPayload.Type(Constants.LOCKOUT_GOALS_TEAMS_PACKET);
      CODEC = new StreamCodec<RegistryFriendlyByteBuf, LockoutGoalsTeamsPayload>() {
         public LockoutGoalsTeamsPayload decode(RegistryFriendlyByteBuf buf) {
            int teamsSize = buf.readInt();
            List<LockoutTeam> teams = new ArrayList(teamsSize);

            for(int i = 0; i < teamsSize; ++i) {
               int teamSize = buf.readInt();
               int color = buf.readInt();
               List<String> playerNames = new ArrayList();

               for(int j = 0; j < teamSize; ++j) {
                  String playerName = buf.readUtf();
                  playerNames.add(playerName);
               }

               teams.add(new LockoutTeam(playerNames, color));
            }

            int size = buf.readInt();
            List<Pair<Pair<String, String>, Integer>> goals = new ArrayList(size);

            for(int i = 0; i < size; ++i) {
               goals.add(new Pair(new Pair(buf.readUtf(), buf.readUtf()), buf.readInt()));
            }

            boolean isRunning = buf.readBoolean();
            return new LockoutGoalsTeamsPayload(teams, goals, isRunning);
         }

         public void encode(RegistryFriendlyByteBuf buf, LockoutGoalsTeamsPayload payload) {
            List<LockoutTeam> teams = payload.teams();
            buf.writeInt(teams.size());

            for(LockoutTeam team : payload.teams()) {
               buf.writeInt(team.getPlayerNames().size());
               buf.writeInt(team.getColor());

               for(String playerName : team.getPlayerNames()) {
                  buf.writeUtf(playerName);
               }
            }

            buf.writeInt(payload.goals().size());

            for(Pair<Pair<String, String>, Integer> goal : payload.goals()) {
               buf.writeUtf((String)((Pair)goal.getA()).getA());
               buf.writeUtf((String)((Pair)goal.getA()).getB());
               buf.writeInt((Integer)goal.getB());
            }

            buf.writeBoolean(payload.isRunning);
         }
      };
   }
}
