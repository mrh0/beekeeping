package github.mrh0.beekeeping.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final String CATEGORY_BEE = "bee";
    public static final String CATEGORY_GENES = "genes";
    public static final String CATEGORY_WORLD = "world";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue LIFETIME_STEP;
    public static ForgeConfigSpec.IntValue BREED_TIME;

    public static ForgeConfigSpec.BooleanValue IGNORE_LIGHT_SATISFACTION;
    public static ForgeConfigSpec.BooleanValue IGNORE_WEATHER_SATISFACTION;
    public static ForgeConfigSpec.BooleanValue IGNORE_TEMPERATURE_SATISFACTION;

    public static ForgeConfigSpec.IntValue LIFETIME_GENE_LONGEST;
    public static ForgeConfigSpec.IntValue LIFETIME_GENE_LONG;
    public static ForgeConfigSpec.IntValue LIFETIME_GENE_NORMAL;
    public static ForgeConfigSpec.IntValue LIFETIME_GENE_SHORT;
    public static ForgeConfigSpec.IntValue LIFETIME_GENE_SHORTEST;

    public static ForgeConfigSpec.IntValue LIGHT_GENE_MIN_LIGHT;

    public static ForgeConfigSpec.BooleanValue BEEHIVE_GENERATION_ENABLED;

    static {
        COMMON_BUILDER.comment("Bee Lifecycle").push(CATEGORY_BEE);

        LIFETIME_STEP = COMMON_BUILDER.comment("Lifetime ticks per game tick.")
                .defineInRange("lifetime_step", 1, 0, Integer.MAX_VALUE);

        BREED_TIME = COMMON_BUILDER.comment("Time to breed bees in ticks.")
                .defineInRange("breed_time", 60, 0, Integer.MAX_VALUE);

        IGNORE_LIGHT_SATISFACTION = COMMON_BUILDER.comment("Ignore light satisfaction check.")
                .define("ignore_light", false);
        IGNORE_WEATHER_SATISFACTION = COMMON_BUILDER.comment("Ignore weather satisfaction check.")
                .define("ignore_weather", false);
        IGNORE_TEMPERATURE_SATISFACTION = COMMON_BUILDER.comment("Ignore temperature satisfaction check.")
                .define("ignore_temperature", false);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Bee Genes").push(CATEGORY_GENES);

        LIFETIME_GENE_LONGEST = COMMON_BUILDER.comment("Lifetime gene longest in seconds.")
                .defineInRange("lifetime_gene_longest", 60*12, 0, Integer.MAX_VALUE);
        LIFETIME_GENE_LONG = COMMON_BUILDER.comment("Lifetime gene long in seconds.")
                .defineInRange("lifetime_gene_long", 60*10, 0, Integer.MAX_VALUE);
        LIFETIME_GENE_NORMAL = COMMON_BUILDER.comment("Lifetime gene normal in seconds.")
                .defineInRange("lifetime_gene_normal", 60*8, 0, Integer.MAX_VALUE);
        LIFETIME_GENE_SHORT = COMMON_BUILDER.comment("Lifetime gene short in seconds.")
                .defineInRange("lifetime_gene_short", 60*6, 0, Integer.MAX_VALUE);
        LIFETIME_GENE_SHORTEST = COMMON_BUILDER.comment("Lifetime gene shortest in seconds.")
                .defineInRange("lifetime_gene_shortest", 60*4, 0, Integer.MAX_VALUE);

        LIGHT_GENE_MIN_LIGHT = COMMON_BUILDER.comment("Minimum required light level to be satisfied.")
                .defineInRange("light_gene_min_light", 11, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("World").push(CATEGORY_WORLD);

        BEEHIVE_GENERATION_ENABLED = COMMON_BUILDER.comment("Enabled beehive world generation")
                .define("beehive_generation_enabled", true);

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
