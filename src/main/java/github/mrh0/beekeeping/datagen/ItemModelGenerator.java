package github.mrh0.beekeeping.datagen;

import com.google.gson.JsonElement;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Beekeeping.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            simpleItem(specie.droneItem);
            simpleItem(specie.princessItem);
            simpleItem(specie.queenItem);
            System.out.println("\"item.beekeeping." + specie.getName() + "_drone\":\"" + Util.capitalize(specie.getName()) + " Drone\",");
            System.out.println("\"item.beekeeping." + specie.getName() + "_princess\":\"" + Util.capitalize(specie.getName()) + " Princess\",");
            System.out.println("\"item.beekeeping." + specie.getName() + "_queen\":\"" + Util.capitalize(specie.getName()) + " Queen\",");
        }
    }

    private ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Beekeeping.MODID,"item/" + item.getRegistryName().getPath()));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.getRegistryName().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Beekeeping.MODID,"item/" + item.getRegistryName().getPath()));
    }
}
