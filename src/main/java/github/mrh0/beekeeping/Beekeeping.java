package github.mrh0.beekeeping;

import com.mojang.logging.LogUtils;
import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Beekeeping.MODID)
public class Beekeeping {
    public static final String MODID = "beekeeping";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Beekeeping() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new ItemGroup();
        Index.register(eventBus);

        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Beekeeping Init!");
    }
}
