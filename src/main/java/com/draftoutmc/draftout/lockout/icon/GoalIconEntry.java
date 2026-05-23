package com.draftoutmc.draftout.lockout.icon;

import java.util.List;
import org.jspecify.annotations.Nullable;

public record GoalIconEntry(String goalId, String data, String label, @Nullable Integer cycleMs, GoalIconAnimationMode animationMode, List<GoalIconFrame> frames) {
}
