package com.draftoutmc.draftout.lockout.goals.kill;

import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import com.draftoutmc.draftout.lockout.interfaces.KillMobGoal;
import com.draftoutmc.draftout.lockout.texture.TextureProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class KillColoredSheepGoal extends KillMobGoal implements TextureProvider {
   private static final Item ITEM;
   private final Identifier texture;
   private final String GOAL_NAME;
   private final DyeColor DYE_COLOR;

   public KillColoredSheepGoal(String id, String data) {
      super(id, data);
      this.texture = Identifier.fromNamespaceAndPath("draftout", "textures/custom/sheep/kill_" + data + "_sheep.png");
      this.DYE_COLOR = GoalDataConstants.getDyeColor(data);
      this.GOAL_NAME = "Kill " + GoalDataConstants.getDyeColorFormatted(this.DYE_COLOR) + " Sheep";
   }

   public EntityType<?> getEntity() {
      return EntityType.SHEEP;
   }

   public DyeColor getDyeColor() {
      return this.DYE_COLOR;
   }

   public String getGoalName() {
      return this.GOAL_NAME;
   }

   public @Nullable ItemStack getTextureItemStack() {
      return ITEM.getDefaultInstance();
   }

   public Identifier getTextureIdentifier() {
      return this.texture;
   }

   static {
      ITEM = Items.SHEEP_SPAWN_EGG;
   }
}
