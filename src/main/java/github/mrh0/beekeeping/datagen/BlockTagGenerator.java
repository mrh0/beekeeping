package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.Beekeeping;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(DataGenerator data, @Nullable ExistingFileHelper existingFileHelper) {
        super(data, Beekeeping.MODID, existingFileHelper);
    }
}
