package com.draftoutmc.draftout.client.gui.ranked.elements;

import java.util.List;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ItemGridTooltip(List<ItemStack> items, int columns) implements TooltipComponent {
}
