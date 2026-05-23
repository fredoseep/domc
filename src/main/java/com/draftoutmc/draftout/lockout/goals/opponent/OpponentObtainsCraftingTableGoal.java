package com.draftoutmc.draftout.lockout.goals.opponent;

import com.draftoutmc.draftout.lockout.interfaces.OpponentObtainsItemGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class OpponentObtainsCraftingTableGoal extends OpponentObtainsItemGoal implements TextureProvider {
   private static final List<Item> ITEMS;
   private static final Identifier TEXTURE;

   public OpponentObtainsCraftingTableGoal(String id, String data) {
      super(id, data);
   }

   public String getMessage(Player player) {
      return player.getName().getString() + " obtained Crafting Table.";
   }

   public String getGoalName() {
      return "Opponent obtains Crafting Table";
   }

   public List<Item> getItems() {
      return ITEMS;
   }

   public Identifier getTextureIdentifier() {
      return TEXTURE;
   }

   public boolean renderTexture(GuiGraphicsExtractor context, int x, int y, int tick) {
      return TextureProvider.super.renderTexture(context, x, y, tick);
   }

   static {
      ITEMS = List.of(Items.CRAFTING_TABLE);
      TEXTURE = Identifier.fromNamespaceAndPath("draftout", "textures/custom/opponent/no_crafting_table.png");
   }
}
