package github.mrh0.beekeeping.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final String CATEGORY_BEE = "bee";
    public static final String CATEGORY_WORLD = "world";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue LIFETIME_STEP;
    public static ForgeConfigSpec.IntValue BREED_TIME;

    static {
        COMMON_BUILDER.comment("Bee Lifecycle").push(CATEGORY_BEE);

        LIFETIME_STEP = COMMON_BUILDER.comment("Lifetime ticks per game tick.")
                .defineInRange("lifetime_step", 1, 0, Integer.MAX_VALUE);

        BREED_TIME = COMMON_BUILDER.comment("Time to breed bees in ticks.")
                .defineInRange("breed_time", 60, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("World").push(CATEGORY_WORLD);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, java.nio.file.Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
}
