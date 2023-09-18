package github.mrh0.beekeeping.item.frame;

import github.mrh0.beekeeping.bee.Satisfaction;
import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FrameItem extends Item {
    public static List<FrameItem> frames = new ArrayList<>();
    private BreedingEvent breedingEvent = (level, pos, drone, princess, queen) -> queen;
    private ProduceEvent produceEvent = (level, pos, produceType, produce) -> produce;
    private SatisfactionEvent satisfactionEvent = (level, pos, satisfactionType, queen, satisfaction) -> satisfaction;
    public final String name;

    public FrameItem(String name) {
        super(new Properties().stacksTo(16).tab(ItemGroup.BEES));
        this.name = name;
        frames.add(this);
    }

    public FrameItem addBreedEvent(BreedingEvent breedingEvent) {
        this.breedingEvent = breedingEvent;
        return this;
    }

    public FrameItem addProduceEvent(ProduceEvent produceEvent) {
        this.produceEvent = produceEvent;
        return this;
    }

    public FrameItem addSatisfactionEvent(SatisfactionEvent satisfactionEvent) {
        this.satisfactionEvent = satisfactionEvent;
        return this;
    }

    public static ItemStack onBreed(ItemStack frameStack, Level level, BlockPos pos, ItemStack drone, ItemStack princess, ItemStack queen) {
        if(frameStack == null)
            return queen;
        if(frameStack.isEmpty())
            return queen;
        if(!(frameStack.getItem() instanceof FrameItem frame))
            return queen;
        return frame.breedingEvent.trigger(level, pos, drone, princess, queen);
    }

    public static ItemStack onProduce(ItemStack frameStack, Level level, BlockPos pos, ProduceEvent.ProduceType produceType, ItemStack produce) {
        if(frameStack == null)
            return produce;
        if(frameStack.isEmpty())
            return produce;
        if(!(frameStack.getItem() instanceof FrameItem frame))
            return produce;
        return frame.produceEvent.trigger(level, pos, produceType, produce);
    }

    public static Satisfaction onSatisfaction(ItemStack frameStack, Level level, BlockPos pos, SatisfactionEvent.SatisfactionType satisfactionType, ItemStack queen, Satisfaction satisfaction) {
        if(frameStack == null)
            return satisfaction;
        if(frameStack.isEmpty())
            return satisfaction;
        if(!(frameStack.getItem() instanceof FrameItem frame))
            return satisfaction;
        return frame.satisfactionEvent.trigger(level, pos, satisfactionType, queen, satisfaction);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("tooltip.beekeeping.frame." + name).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        super.appendHoverText(stack, level, list, flag);
    }
}
