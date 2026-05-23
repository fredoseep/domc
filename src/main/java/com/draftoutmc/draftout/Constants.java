package com.draftoutmc.draftout;

import net.minecraft.resources.Identifier;

public class Constants {
   public static final String NAMESPACE = "draftout";
   public static final int MIN_BOARD_SIZE = 3;
   public static final int MAX_BOARD_SIZE = 7;
   public static final Identifier LOCKOUT_GOALS_TEAMS_PACKET = Identifier.fromNamespaceAndPath("draftout", "lockout_goals_teams");
   public static final Identifier START_LOCKOUT_PACKET = Identifier.fromNamespaceAndPath("draftout", "start_lockout");
   public static final Identifier UPDATE_TOOLTIP = Identifier.fromNamespaceAndPath("draftout", "update_tooltip");
   public static final Identifier COMPLETE_TASK_PACKET = Identifier.fromNamespaceAndPath("draftout", "complete_task");
   public static final Identifier END_LOCKOUT_PACKET = Identifier.fromNamespaceAndPath("draftout", "end_lockout");
   public static final Identifier UPDATE_TIMER_PACKET = Identifier.fromNamespaceAndPath("draftout", "update_timer");
   public static final Identifier GUI_IDENTIFIER = Identifier.fromNamespaceAndPath("draftout", "gui");
   public static final int GUI_PADDING = 2;
   public static final int GUI_PADDING_BOTTOM = 13;
   public static final int GUI_SLOT_SIZE = 18;
   public static final Identifier GUI_CENTER_IDENTIFIER = Identifier.fromNamespaceAndPath("draftout", "gui_center");
   public static final int GUI_CENTER_PADDING = 7;
   public static final int GUI_CENTER_SLOT_SIZE = 18;
   public static final int GUI_CENTER_HOVERED_COLOR = -2130706433;
   public static final Identifier GUI_DRAFT_PICKS = Identifier.fromNamespaceAndPath("draftout", "draft_picks");
   public static final Identifier BUTTON_IDENTIFIER = Identifier.fromNamespaceAndPath("draftout", "button");
   public static final Identifier BUTTON_MAIN_IDENTIFIER = Identifier.fromNamespaceAndPath("draftout", "button_main");
   public static final Identifier TIMELINE_IDENTIFIER = Identifier.fromNamespaceAndPath("draftout", "timeline");
}
