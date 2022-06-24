package github.mrh0.beekeeping.bee;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record Produce(Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare,
					  double rareChanceUnsatisfied, double rareChanceSatisfied) {
}
