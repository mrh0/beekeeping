package github.mrh0.beekeeping.datagen.loot;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

public class BlockLootTables extends BlockLoot {

    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();

    @Override
    protected void addTables() {
        this.dropSelf(Index.ANALYZER_BLOCK.get());
        this.dropSelf(Index.APIARY_BLOCK.get());

        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(!specie.hasBeehive())
                return;
            this.add(specie.beehive.block.get(),
                    (block) -> beehiveLootTable(specie.beehive.block.get(), specie.queenItem, specie.princessItem, specie.droneItem));
        }
    }

    protected static LootTable.Builder beehiveLootTable(Block beehive, Item queen, Item princess, Item drone) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(beehive)))
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(0.3F)).add(LootItem.lootTableItem(queen)))
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(princess)))
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(drone)))
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(0.5F)).add(LootItem.lootTableItem(drone)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Index.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}