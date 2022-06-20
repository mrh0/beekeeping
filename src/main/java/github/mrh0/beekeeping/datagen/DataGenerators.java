package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.datagen.graphics.BeeIconGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Beekeeping.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) throws IOException {
        BeeIconGenerator.makeAll();
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        var blockTags = new BlockTagGenerator(generator, existingFileHelper);
        generator.addProvider(new RecipeGenerator(generator));
        generator.addProvider(new ItemModelGenerator(generator, existingFileHelper));
        generator.addProvider(new BlockTagGenerator(generator, existingFileHelper));
        generator.addProvider(new ItemTagGenerator(generator, blockTags, existingFileHelper));
        generator.addProvider(new LootTableGenerator(generator));
        generator.addProvider(new BlockStateGenerator(generator, existingFileHelper));
    }
}