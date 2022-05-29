package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlock;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.screen.AnalyzerMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Index {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Beekeeping.MODID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Beekeeping.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Beekeeping.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Beekeeping.MODID);

    static {
        species();
        blocks();
        items();
        blockEntities();
        menus();
        tags();
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        MENUS.register(eventBus);
    }

    public static void species() {
        var r = SpeciesRegistry.instance;
        r.register(new Specie("common", 0xFFb9c2cf));
    }

    public static void items() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            ITEMS.register(specie.getName() + "_drone", () -> specie.buildDroneItem());
            ITEMS.register(specie.getName() + "_princess", () -> specie.buildPrincessItem());
            ITEMS.register(specie.getName() + "_queen", () -> specie.buildQueenItem());
        }
    }

    public static RegistryObject<AnalyzerBlock> ANALYZER_BLOCK;

    public static void blocks() {
        ANALYZER_BLOCK = BLOCKS.register("analyzer", () -> new AnalyzerBlock());
        ITEMS.register("analyzer", () -> new BlockItem(ANALYZER_BLOCK.get(), new Item.Properties().tab(ItemGroup.BEES)));
    }

    public static RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER_BLOCK_ENTITY;

    public static void blockEntities() {
        ANALYZER_BLOCK_ENTITY = BLOCK_ENTITIES.register("analyzer_block_entity", () ->
                BlockEntityType.Builder.of(AnalyzerBlockEntity::new, ANALYZER_BLOCK.get()).build(null));
    }

    public static RegistryObject<MenuType<AnalyzerMenu>> ANALYZER_MENU;

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void menus() {
        ANALYZER_MENU = registerMenuType(AnalyzerMenu::new, "analyzer");
    }

    public static TagKey<Item> BEES_TAG;

    public static void tags() {
        BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "bees"));
    }
}
