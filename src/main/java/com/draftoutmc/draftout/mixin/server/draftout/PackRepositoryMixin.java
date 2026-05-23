package com.draftoutmc.draftout.mixin.server.draftout;

import com.draftoutmc.draftout.match.data.LockoutMatchData;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PackRepository.class})
public class PackRepositoryMixin {
   @Inject(
      method = {"setSelected"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void stripUserPacksInMatch(Collection<String> packs, CallbackInfo ci) {
      if (LockoutMatchData.isInMatch()) {
         PackRepository packRepository = (PackRepository)(Object)this;
         if (packRepository == Minecraft.getInstance().getResourcePackRepository()) {
            PackRepository self = (PackRepository)(Object)this;
            List<String> filtered = packs.stream().filter((id) -> {
               Pack pack = self.getPack(id);
               return pack != null && (pack.isRequired() || pack.getPackSource() != PackSource.DEFAULT);
            }).toList();
            if (filtered.size() != packs.size()) {
               self.setSelected(filtered);
               ci.cancel();
            }
         }
      }
   }

   @Inject(
      method = {"openAllSelected"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void filterPacksInMatch(CallbackInfoReturnable<List<PackResources>> cir) {
      if (LockoutMatchData.isInMatch()) {
         PackRepository packRepository = (PackRepository)(Object)this;
         if (packRepository == Minecraft.getInstance().getResourcePackRepository()) {
            PackRepository self = (PackRepository)(Object)this;
            List<PackResources> filtered = (List)self.getSelectedPacks().stream().filter((pack) -> pack.isRequired() || pack.getPackSource() != PackSource.DEFAULT).map(Pack::open).collect(ImmutableList.toImmutableList());
            cir.setReturnValue(filtered);
         }
      }
   }
}
