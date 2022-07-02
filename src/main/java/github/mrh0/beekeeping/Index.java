package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlock;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlock;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import github.mrh0.beekeeping.group.ItemGroup;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.BiomeDictionary;
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
        r.register(new Specie("common", 0xFFfff2cc)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.PLAINS), 2, 16));

        r.register(new Specie("forest", 0xFF93c47d)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.FOREST), 4, 10));

        r.register(new Specie("tempered", 0xFFb6d7a8)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .setTemperatureGene(Gene::random5Narrow)
                .breedFrom("common", "forest"));

        r.register(new Specie("tropical", 0xFF6aa84f)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.JUNGLE), 4, 10)
                .setLifetimeGene(Gene::random5Narrow)
                .setPreferredTemperature(BiomeTemperature.WARM));

        r.register(new Specie("coco", 0xFF783f04)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.COCOA_BEANS, 0.7d, 0.9d)
                .setPreferredTemperature(BiomeTemperature.WARM)
                .breedFrom("tropical", "fair"));

        r.register(new Specie("upland", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.SAVANNA), 4, 16)
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Specie("dune", 0xFFfbbc04)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.SANDY) && types.contains(BiomeDictionary.Type.HOT), 3, 12)
                .setLifetimeGene(Gene::random5Narrow)
                .setWeatherGene(Gene::strict)
                .setPreferredTemperature(BiomeTemperature.WARMEST));

        r.register(new Specie("snowy", 0xFFefefef)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.SNOWY), 4, 16)
                .setTemperatureGene(Gene::random3Low)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Specie("frozen", 0xFFd0e0e3)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .setPreferredTemperature(BiomeTemperature.COLD)
                .breedFrom("snowy", "dugout"));

        r.register(new Specie("glacial", 0xFFa2c4c9)
                .setFoil()
                .setProduce(Items.HONEYCOMB, 9, 13)
                .setPreferredTemperature(BiomeTemperature.COLDEST)
                .breedFrom("frozen", "nocturnal"));

        r.register(new Specie("fungal", 0xFF660000)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.RED_MUSHROOM, 0.5d, 0.8d)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.MUSHROOM) || types.contains(BiomeDictionary.Type.SWAMP), 3, 10)
                .setTemperatureGene(Gene::random3High)
                .setProduceGene(Gene::random5High));

        r.register(new Specie("mossy", 0xFF38761d)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.MOSS_BLOCK, 0.5d, 0.8d)
                .setTemperatureGene(Gene::random3Low)
                .setProduceGene(Gene::normal5)
                .breedFrom("dugout", "fungal"));

        r.register(new Specie("fair", 0xFF00ff00)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.SUGAR, 0.7d, 0.9d)
                .setFoil()
                .breedFrom("mossy", "frozen"));

        r.register(new Specie("dugout", 0xFF7f6000)
                .setProduce(Items.HONEYCOMB, 9, 13)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.UNDERGROUND), 8, 1, PlacementUtils.FULL_RANGE)
                .setTemperatureGene(Gene::random3High)
                .setLightGene(Gene::any)
                .setPreferredTemperature(BiomeTemperature.COLD));

        r.register(new Specie("nocturnal", 0xFF073763)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.GLOWSTONE_DUST, 0.5d, 0.8d)
                .setNocturnal()
                .breedFrom("ender", "dugout"));

        r.register(new Specie("scorched", 0xFFff9900)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.COAL, 0.4d, 0.8d)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.NETHER), 16, 1, PlacementUtils.FULL_RANGE)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark());

        r.register(new Specie("magmatic", 0xFFff6d01)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.MAGMA_CREAM, 0.4d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark()
                .breedFrom("scorched", "dune"));

        r.register(new Specie("infernal", 0xFFff0000)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.GUNPOWDER, 0.4d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setDark());

        r.register(new Specie("demonic", 0xFF990000)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.BLAZE_POWDER, 0.4d, 0.8d)
                .setPreferredTemperature(BiomeTemperature.WARMEST)
                .setLightGene(Gene::any)
                .setFoil()
                .setDark());

        r.register(new Specie("ender", 0xFF134f5c)
                .setProduce(Items.HONEYCOMB, 9, 13, Items.ENDER_PEARL, 0.2d, 0.4d)
                .addBeehive(types -> types.contains(BiomeDictionary.Type.END), 2, 16, PlacementUtils.HEIGHTMAP)
                .setPreferredTemperature(BiomeTemperature.COLD)
                .setLightGene(Gene::any)
                .setDark());
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

        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(!specie.hasBeehive())
                continue;
            specie.beehive.block = BLOCKS.register(specie.beehive.getName(), () -> new BeehiveBlock(BlockBehaviour.Properties.of(Blocks.BEEHIVE.defaultBlockState().getMaterial())));
            ITEMS.register(specie.beehive.getName(), () -> new BlockItem(specie.beehive.block.get(), new Item.Properties().tab(ItemGroup.BEES)));
        }
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
    public static TagKey<Block> BEEHIVE_TAG;

    public static void tags() {
        BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "bees"));
        DRONE_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "drone_bees"));
        PRINCESS_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "princess_bees"));
        QUEEN_BEES_TAG = ItemTags.create(new ResourceLocation("beekeeping", "queen_bees"));
        BEEHIVE_TAG = BlockTags.create(new ResourceLocation("beekeeping", "beehives"));
    }

    //  RECIPE
    public static RegistryObject<RecipeSerializer<BeeBreedingRecipe>> BEE_BREEDING_RECIPE;
    public static RegistryObject<RecipeSerializer<BeeProduceRecipe>> BEE_PRODUCE_RECIPE;

    public static void recipes() {
        BEE_BREEDING_RECIPE = SERIALIZERS.register("bee_breeding", () -> BeeBreedingRecipe.Serializer.INSTANCE);
        BEE_PRODUCE_RECIPE = SERIALIZERS.register("bee_produce", () -> BeeProduceRecipe.Serializer.INSTANCE);
    }
}
