package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

public class ItemTagGenerator extends ItemTagsProvider {

    public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blocks, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blocks, Beekeeping.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            tag(Index.BEES_TAG)
                .add(specie.droneItem)
                .add(specie.princessItem)
                .add(specie.queenItem);
            tag(Index.DRONE_BEES_TAG).add(specie.droneItem);
            tag(Index.PRINCESS_BEES_TAG).add(specie.princessItem);
            tag(Index.QUEEN_BEES_TAG).add(specie.queenItem);
        }
    }
}
