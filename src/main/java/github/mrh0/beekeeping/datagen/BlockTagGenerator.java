package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(DataGenerator data, @Nullable ExistingFileHelper existingFileHelper) {
        super(data, Beekeeping.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(!specie.hasBeehive()) continue;
            tag(Index.BEEHIVE_TAG).add(specie.beehive.block.get());
            tag(BlockTags.MINEABLE_WITH_AXE).add(specie.beehive.block.get());
        }

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(Index.APIARY_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Index.ANALYZER_BLOCK.get());
    }
}
