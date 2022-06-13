package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlock;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlock;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.openjdk.nashorn.internal.ir.annotations.Ignore;

public class Index {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Beekeeping.MODID);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Beekeeping.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Beekeeping.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Beekeeping.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Beekeeping.MODID);

    static {
        species();
        blocks();
        items();
        blockEntities();
        menus();
        tags();
        recipes();
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        MENUS.register(eventBus);
        SERIALIZERS.register(eventBus);
    }

    //  SPECIE
    public static void species() {
        var r = SpeciesRegistry.instance;
        r.register(new Specie("common", 0xFFb9c2cf)
                .setLifetimeGene(Gene::randomWide))
                .setPreferredBiomes(BiomeTags.IS_FOREST, BiomeTags.IS_BEACH, BiomeTags.IS_TAIGA);
        r.register(new Specie("forest", 0xFF93c47d)
                .setLifetimeGene(Gene::randomWide))
                .setPreferredBiomes(BiomeTags.IS_FOREST);
        r.register(new Specie("tempered", 0xFFb6d7a8)
                .setLifetimeGene(Gene::randomWide))
                .setPreferredBiomes(BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA);
    }

    //  ITEM
    public static void items() {
        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            ITEMS.register(specie.getName() + "_drone", () -> specie.buildDroneItem());
            ITEMS.register(specie.getName() + "_princess", () -> specie.buildPrincessItem());
            ITEMS.register(specie.getName() + "_queen", () -> specie.buildQueenItem());
        }
    }

    //  BLOCK
    public static RegistryObject<AnalyzerBlock> ANALYZER_BLOCK;
    public static RegistryObject<ApiaryBlock> APIARY_BLOCK;

    public static void blocks() {
        ANALYZER_BLOCK = BLOCKS.register("analyzer", () -> new AnalyzerBlock());
        ITEMS.register("analyzer", () -> new BlockItem(ANALYZER_BLOCK.get(), new Item.Properties().tab(ItemGroup.BEES)));
        APIARY_BLOCK = BLOCKS.register("apiary", () -> new ApiaryBlock());
        ITEMS.register("apiary", () -> new BlockItem(APIARY_BLOCK.get(), new Item.Properties().tab(ItemGroup.BEES)));
    }

    //  BLOCK ENTITY
    public static RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER_BLOCK_ENTITY;
    public static RegistryObject<BlockEntityType<ApiaryBlockEntity>> APIARY_BLOCK_ENTITY;

    public static void blockEntities() {
        ANALYZER_BLOCK_ENTITY = BLOCK_ENTITIES.register("analyzer_block_entity", () ->
                BlockEntityType.Builder.of(AnalyzerBlockEntity::new, ANALYZER_BLOCK.get()).build(null));
        APIARY_BLOCK_ENTITY = BLOCK_ENTITIES.register("apiary_block_entity", () ->
                BlockEntityType.Builder.of(ApiaryBlockEntity::new, APIARY_BLOCK.get()).build(null));
    }

    //   MENU
    public static RegistryObject<MenuType<AnalyzerMenu>> ANALYZER_MENU;
    public static RegistryObject<MenuType<ApiaryMenu>> APIARY_MENU;

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void menus() {
        ANALYZER_MENU = registerMenuType(AnalyzerMenu::new, "analyzer");
        APIARY_MENU = registerMenuType(ApiaryMenu::new, "apiary");
    }

    //  TAG
    public static TagKey<Item> BEES_TAG;
    public static TagKey<Item> DRONE_BEES_TAG;
    public static TagKey<Item> PRINCESS_BEES_TAG;
    public static TagKey<Item> QUEEN_BEES_TAG;

    public static void tags() {
        BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "bees"));
        DRONE_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "drone_bees"));
        PRINCESS_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "princess_bees"));
        QUEEN_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "queen_bees"));
    }

    //  RECIPE
    public static RegistryObject<RecipeSerializer<BeeBreedingRecipe>> BEE_BREEDING_RECIPE;
    public static RegistryObject<RecipeSerializer<BeeProduceRecipe>> BEE_PRODUCE_RECIPE;

    public static void recipes() {
        BEE_BREEDING_RECIPE = SERIALIZERS.register("bee_breeding", () -> BeeBreedingRecipe.Serializer.INSTANCE);
        BEE_PRODUCE_RECIPE = SERIALIZERS.register("bee_produce", () -> BeeProduceRecipe.Serializer.INSTANCE);
    }
}
