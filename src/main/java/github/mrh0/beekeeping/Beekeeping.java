package github.mrh0.beekeeping;

import com.mojang.logging.LogUtils;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.event.ClientEvents;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.network.TogglePacket;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerScreen;
import github.mrh0.beekeeping.screen.apiary.ApiaryScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

@Mod(Beekeeping.MODID)
public class Beekeeping {
    public static final String MODID = "beekeeping";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    public Beekeeping() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new ItemGroup();
        Index.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::postInit);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("beekeeping-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Beekeeping Init!");
    }

    public void postInit(FMLLoadCompleteEvent evt) {
        int i = 0;
        NETWORK.registerMessage(i++, TogglePacket.class, TogglePacket::encode, TogglePacket::decode, TogglePacket::handle);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(Index.ANALYZER_MENU.get(), AnalyzerScreen::new);
        MenuScreens.register(Index.APIARY_MENU.get(), ApiaryScreen::new);
    }

    public static ResourceLocation get(String resource) {
        return new ResourceLocation(Beekeeping.MODID, resource);
    }
}
