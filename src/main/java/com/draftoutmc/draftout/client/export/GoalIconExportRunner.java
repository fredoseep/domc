package com.draftoutmc.draftout.client.export;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagLoader.ElementLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class GoalIconExportRunner {
   private static boolean itemComponentsBound;
   private static boolean itemTagsBound;
   private boolean exporting;

   private GoalIconExportRunner() {
   }

   public static void initializeIfRequested() {
      if (isExportMode()) {
         GoalIconExportRunner runner = new GoalIconExportRunner();
         ClientTickEvents.END_CLIENT_TICK.register(runner::tick);
         System.out.println("[DraftoutGoalIconExport] export mode enabled");
      }
   }

   public static boolean isExportMode() {
      return GoalIconExportEnvironment.isExportMode();
   }

   private void tick(Minecraft client) {
      if (!this.exporting && isReadyToExport(client) && itemTagsBound && itemComponentsBound) {
         this.exporting = true;
         System.out.println("[DraftoutGoalIconExport] opening export screen from " + client.screen.getClass().getName());
         client.setScreen(new GoalIconExportScreen());
      }
   }

   public static boolean isReadyToExport(Minecraft client) {
      return client.screen != null && client.getOverlay() == null && client.isGameLoadFinished();
   }

   public static void ensureBuiltInItemTagsBound(Minecraft client) {
      if (isReadyToExport(client)) {
         if (!itemTagsBound) {
            CloseableResourceManager resourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, List.of(ServerPacksSource.createVanillaPackSource()));

            try {
               TagLoader.ElementLookup<Holder<Item>> itemLookup = (ElementLookup<Holder<Item>>) ElementLookup.fromFrozenRegistry(BuiltInRegistries.ITEM);
               Map<TagKey<Item>, List<Holder<Item>>> tags = TagLoader.loadTagsForRegistry(resourceManager, Registries.ITEM, itemLookup);
               BuiltInRegistries.ITEM.prepareTagReload(new TagLoader.LoadResult(Registries.ITEM, tags)).apply();
               itemTagsBound = true;
               if (isExportMode()) {
                  System.out.println("[DraftoutGoalIconExport] bound built-in item tags for export");
               }
            } catch (Throwable var5) {
               try {
                  resourceManager.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            resourceManager.close();
         }
      }
   }

   public static void ensureBuiltInItemComponentsBound(Minecraft client) {
      if (isReadyToExport(client)) {
         if (!itemComponentsBound && !areBuiltInItemComponentsBound()) {
            BuiltInRegistries.ITEM.listElements().forEach((holder) -> {
               Item item = (Item)holder.value();
               Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
               DataComponentMap components = DataComponentMap.builder().addAll(DataComponents.COMMON_ITEM_COMPONENTS).set(DataComponents.ITEM_MODEL, itemId).set(DataComponents.ITEM_NAME, Component.literal(itemId.toString())).build();
               holder.bindComponents(components);
            });
            itemComponentsBound = true;
            if (isExportMode()) {
               System.out.println("[DraftoutGoalIconExport] bound built-in item render components for export");
            }

         } else {
            itemComponentsBound = true;
         }
      }
   }

   private static boolean areBuiltInItemComponentsBound() {
      Identifier airId = BuiltInRegistries.ITEM.getKey(Items.AIR);
      return (Boolean)BuiltInRegistries.ITEM.get(airId).map(Holder.Reference::areComponentsBound).orElse(false);
   }
}
