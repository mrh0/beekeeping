package github.mrh0.beekeeping.world;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.world.gen.BeehiveGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Beekeeping.MODID)
public class BeekeepingWorld {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        BeehiveGeneration.generateSurfaceBeehives(event);
    }
}