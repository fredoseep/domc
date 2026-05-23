package com.draftoutmc.draftout.lockout.goals.obtain;

import com.draftoutmc.draftout.lockout.interfaces.ObtainItemsGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import com.draftoutmc.draftout.mixin.server.PlayerInventoryAccessor;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ObtainShieldWithBannerGoal extends ObtainItemsGoal implements TextureProvider {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/apply_banner_shield.png");

   public ObtainShieldWithBannerGoal(String id, String data) {
      super(id, data);
   }

   public String getGoalName() {
      return "Decorate Shield with Banner";
   }

   public List<Item> getItems() {
      return null;
   }

   public boolean satisfiedBy(Inventory playerInventory) {
      for(ItemStack item : ((PlayerInventoryAccessor)playerInventory).getItems()) {
         if (item != null && !item.isEmpty() && item.getItem().equals(Items.SHIELD) && item.get(DataComponents.BASE_COLOR) != null) {
            return true;
         }
      }

      ItemStack offHandItem = ((PlayerInventoryAccessor)playerInventory).getEquipment().get(EquipmentSlot.OFFHAND);
      return offHandItem != null && !offHandItem.isEmpty() && offHandItem.getItem().equals(Items.SHIELD) && offHandItem.get(DataComponents.BASE_COLOR) != null;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      return TextureProvider.super.renderTexture(context, x, y, tick);
   }
}
