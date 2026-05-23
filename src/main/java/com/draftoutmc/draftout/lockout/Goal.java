package com.draftoutmc.draftout.lockout;

import com.draftoutmc.draftout.LockoutTeam;
import com.draftoutmc.draftout.client.LockoutClient;
import com.draftoutmc.draftout.lockout.texture.CustomTextureRenderer;
import com.draftoutmc.draftout.match.PendingChatMessage;
import java.util.Objects;
import lombok.Generated;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

public abstract class Goal {
   private final String id;
   private final String data;
   private boolean isCompleted = false;
   private boolean markedAsCompleted = false;
   private PendingChatMessage pendingChatMessage;
   private LockoutTeam completedTeam;
   private long completedAtMs;

   public Goal(String id, String data) {
      this.id = id;
      this.data = data;
   }

   public abstract String getGoalName();

   public abstract @Nullable ItemStack getTextureItemStack();

   public void setCompleted(boolean isCompleted, LockoutTeam team) {
      this.isCompleted = isCompleted;
      this.completedTeam = team;
   }

   public final void render(GuiGraphicsExtractor context, Font textRenderer, int x, int y) {
      if (this instanceof CustomTextureRenderer customTextureRenderer) {
         if (customTextureRenderer.renderTexture(context, x, y, LockoutClient.RENDER_TICK)) {
            return;
         }
      }

      if (this.getTextureItemStack() != null) {
         context.item(this.getTextureItemStack(), x, y);
      }

      context.itemDecorations(textRenderer, this.getTextureItemStack(), x, y);
   }

   public void renderScaled(GuiGraphicsExtractor context, Font textRenderer, int x, int y, float scale) {
      if (scale == 1.0F) {
         this.render(context, textRenderer, x, y);
      } else {
         Matrix3x2f pose = context.pose();
         pose.translate((float)x, (float)y);
         pose.scale(scale, scale);
         pose.translate((float)(-x), (float)(-y));
         this.render(context, textRenderer, x, y);
         pose.translate((float)x, (float)y);
         pose.scale(1.0F / scale, 1.0F / scale);
         pose.translate((float)(-x), (float)(-y));
      }
   }

   public boolean hasData() {
      return this.data != null && !Objects.equals(this.data, "null");
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Goal goal = (Goal)o;
         return this.id.equals(goal.id) && Objects.equals(this.data, goal.data);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.data});
   }

   @Generated
   public String getId() {
      return this.id;
   }

   @Generated
   public String getData() {
      return this.data;
   }

   @Generated
   public boolean isCompleted() {
      return this.isCompleted;
   }

   @Generated
   public boolean isMarkedAsCompleted() {
      return this.markedAsCompleted;
   }

   @Generated
   public void setMarkedAsCompleted(boolean markedAsCompleted) {
      this.markedAsCompleted = markedAsCompleted;
   }

   @Generated
   public PendingChatMessage getPendingChatMessage() {
      return this.pendingChatMessage;
   }

   @Generated
   public void setPendingChatMessage(PendingChatMessage pendingChatMessage) {
      this.pendingChatMessage = pendingChatMessage;
   }

   @Generated
   public LockoutTeam getCompletedTeam() {
      return this.completedTeam;
   }

   @Generated
   public long getCompletedAtMs() {
      return this.completedAtMs;
   }

   @Generated
   public void setCompletedAtMs(long completedAtMs) {
      this.completedAtMs = completedAtMs;
   }
}
