package github.mrh0.beekeeping.item;

import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.world.item.Item;

public class FrameItem extends Item {
    private IFrameBreedingEvent breedingEvent;
    private IFrameProduceEvent produceEvent;
    private IFrameSatisfactionEvent satisfactionEvent;

    public FrameItem() {
        super(new Properties().stacksTo(16).tab(ItemGroup.BEES));
    }

    public FrameItem onBreed(IFrameBreedingEvent breedingEvent) {
        this.breedingEvent = breedingEvent;
        return this;
    }

    public FrameItem onProduce(IFrameProduceEvent produceEvent) {
        this.produceEvent = produceEvent;
        return this;
    }

    public FrameItem onSatisfaction(IFrameSatisfactionEvent satisfactionEvent) {
        this.satisfactionEvent = satisfactionEvent;
        return this;
    }
}
