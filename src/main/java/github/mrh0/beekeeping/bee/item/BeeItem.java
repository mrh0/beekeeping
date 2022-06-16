package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.genes.*;
import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BeeItem extends Item {
    private final Specie specie;
    private final boolean foil;

    public BeeItem(Specie specie, Properties props, boolean foil) {
        super(props.tab(ItemGroup.BEES));
        this.specie = specie;
        this.foil = foil;
    }

    public enum BeeType {
        DRONE("drone"),
        PRINCESS("princess"),
        QUEEN("queen");
        public final String name;

        BeeType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public boolean isFoil(ItemStack item) {
        return foil;
    }

    public abstract BeeType getType();

    public abstract ResourceLocation getResourceLocation();

    public static void setAnalyzed(CompoundTag tag, boolean analyzed) {
        tag.putBoolean("analyzed", analyzed);
    }

    public static boolean getAnalyzed(CompoundTag tag) {
        return tag.getBoolean("analyzed");
    }

    public static boolean isAnalyzed(ItemStack stack) {
        if(stack.isEmpty())
            return false;
        if(!(stack.getItem() instanceof BeeItem))
            return false;
        if(stack.getTag() == null)
            return false;
        return getAnalyzed(stack.getTag());
    }

    public static void setHealth(CompoundTag tag, int health) {
        tag.putInt("health", health);
    }

    public static int getHealth(CompoundTag tag) {
        return tag.getInt("health");
    }

    public static void init(ItemStack stack) {
        if(stack.isEmpty())
            return;
        if(!(stack.getItem() instanceof BeeItem))
            return;
        if(stack.getTag() == null)
            stack.setTag(new CompoundTag());
        CompoundTag tag = stack.getTag();
        if(!(stack.getItem() instanceof BeeItem))
            return;
        BeeItem beeItem = (BeeItem) stack.getItem();

        init(tag, beeItem,
                Gene.eval(beeItem.specie.lifetimeGene),
                Gene.eval(beeItem.specie.weatherGene),
                Gene.eval(beeItem.specie.temperatureGene),
                Gene.eval(beeItem.specie.lightGene),
                Gene.eval(beeItem.specie.produceGene)
        );

    }

    public static void init(CompoundTag tag, BeeItem beeItem, int lifetimeGene, int weatherGene, int temperatureGene, int lightGene, int produceGene) {
        setAnalyzed(tag, false);

        //System.out.println(lifetimeGene + ":" + weatherGene + ":" + temperatureGene + ":" + lightGene + ":" + produceGene);
        //System.out.println(LifetimeGene.of(lifetimeGene).getName() + ":" + WeatherToleranceGene.of(biomeGene).getName() + ":" + LightToleranceGene.of(lightGene).getName() + ":" + RareProduceGene.of(produceGene).getName());

        LifetimeGene.set(tag, lifetimeGene);
        WeatherToleranceGene.set(tag, weatherGene);
        TemperatureToleranceGene.set(tag, temperatureGene);
        LightToleranceGene.set(tag, lightGene);
        RareProduceGene.set(tag, produceGene);

        setHealth(tag, LifetimeGene.of(LifetimeGene.get(tag)).getTime());
    }

    //  Remove
    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(stack.getTag() == null)
            return false;
        return getAnalyzed(stack.getTag());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (getHealthOf(stack) * 13d);
    }

    public static double getHealthOf(ItemStack stack) {
        if(stack.getTag() == null)
            return 1d;
        double health = getHealth(stack.getTag());
        double max = LifetimeGene.of(LifetimeGene.get(stack.getTag())).getTime();
        return health/max;
    }

    ChatFormatting[] formatting = {
            ChatFormatting.DARK_AQUA,
            ChatFormatting.AQUA,
            ChatFormatting.YELLOW,
            ChatFormatting.GOLD,
            ChatFormatting.RED
    };

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        if(stack.isEmpty())
            return;
        if(!(stack.getItem() instanceof BeeItem))
            return;
        if(stack.getTag() == null || !isAnalyzed(stack)) {
            list.add(new TranslatableComponent("tooltip.beekeeping.gene.not_analyzed").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            return;
        }

        CompoundTag tag = stack.getTag();
        list.add(new TranslatableComponent("tooltip.beekeeping.gene.lifetime")
                .append(": ").append(new TextComponent(LifetimeGene.of(LifetimeGene.get(tag)).getName()).withStyle(formatting[LifetimeGene.get(tag)])));
        list.add(new TranslatableComponent("tooltip.beekeeping.gene.weather")
                .append(": ").append(new TextComponent(WeatherToleranceGene.of(WeatherToleranceGene.get(tag)).getName()).withStyle(formatting[WeatherToleranceGene.get(tag)])));
        list.add(new TranslatableComponent("tooltip.beekeeping.gene.temperature")
                .append(": ").append(new TextComponent(TemperatureToleranceGene.of(TemperatureToleranceGene.get(tag)).getName()).withStyle(formatting[TemperatureToleranceGene.get(tag)])));
        list.add(new TranslatableComponent("tooltip.beekeeping.gene.light")
                .append(": ").append(new TextComponent(LightToleranceGene.of(LightToleranceGene.get(tag)).getName()).withStyle(formatting[LightToleranceGene.get(tag)])));
        list.add(new TranslatableComponent("tooltip.beekeeping.gene.produce")
                .append(": ").append(new TextComponent(RareProduceGene.of(RareProduceGene.get(tag)).getName()).withStyle(formatting[RareProduceGene.get(tag)])));

        list.add(new TextComponent("health: " + getHealth(stack.getTag())));
    }

    public static Specie of(ItemStack stack) {
        if(stack.isEmpty())
            return null;
        if(!(stack.getItem() instanceof BeeItem))
            return null;
        return ((BeeItem) stack.getItem()).specie;
    }

    public static boolean is(ItemStack stack, Specie specie) {
        return of(stack) == specie;
    }
}
