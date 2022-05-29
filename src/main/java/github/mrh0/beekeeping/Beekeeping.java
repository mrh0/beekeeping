package github.mrh0.beekeeping;

import com.mojang.logging.LogUtils;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.screen.AnalyzerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Beekeeping.MODID)
public class Beekeeping {
    public static final String MODID = "beekeeping";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Beekeeping() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new ItemGroup();
        Index.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Beekeeping Init!");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(Index.ANALYZER_MENU.get(), AnalyzerScreen::new);
    }
}
