package github.mrh0.beekeeping.bee;

import net.minecraft.network.chat.TranslatableComponent;

public enum Satisfaction {
    SATISFIED(0, "tooltip.beekeeping.apiary.satisfied"),
    UNSATISFIED(1, "tooltip.beekeeping.apiary.unsatisfied"),
    NOT_WORKING(2, "tooltip.beekeeping.apiary.not_working");

    public final int index;
    public final TranslatableComponent component;

    Satisfaction(int index, String key) {
        this.index = index;
        this.component = new TranslatableComponent(key);
    }

    public static Satisfaction calc(Satisfaction...list) {
        Satisfaction satisfaction = SATISFIED;
        for(Satisfaction s : list) {
            if(s == NOT_WORKING)
                return NOT_WORKING;
            if(s == UNSATISFIED)
                satisfaction = UNSATISFIED;
        }
        return satisfaction;
    }

    @Deprecated
    public static Satisfaction map(int value, int satisfied, int unsatisfied) {
        if(value < unsatisfied)
            return Satisfaction.NOT_WORKING;
        else if(value < satisfied)
            return Satisfaction.UNSATISFIED;
        return  Satisfaction.SATISFIED;
    }
}