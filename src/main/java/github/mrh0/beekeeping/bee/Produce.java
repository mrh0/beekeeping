package github.mrh0.beekeeping.bee;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record Produce(Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare,
					  int rareCountUnsatisfied, int rareCountSatisfied,
					  double rareChanceUnsatisfied, double rareChanceSatisfied) {
}
