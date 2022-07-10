package github.mrh0.beekeeping.blocks.apiary;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Satisfaction;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.bee.genes.RareProduceGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.config.Config;
import github.mrh0.beekeeping.network.IHasToggleOption;
import github.mrh0.beekeeping.network.TogglePacket;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ApiaryBlockEntity extends BlockEntity implements MenuProvider, IHasToggleOption {

    private static final int LIFETIME_STEP = Config.LIFETIME_STEP.get();
    public static final int BREED_TIME = Config.BREED_TIME.get();

    protected final ContainerData data;
    public ApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(Index.APIARY_BLOCK_ENTITY.get(), pos, state);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> breedProgressTime;
                    case 1 -> satisfactionCache == null ? 0 : satisfactionCache.ordinal();
                    case 2 -> weatherSatisfactionCache == null ? 0 : weatherSatisfactionCache.ordinal();
                    case 3 -> temperatureSatisfactionCache == null ? 0 : temperatureSatisfactionCache.ordinal();
                    case 4 -> lightSatisfactionCache == null ? 0 : lightSatisfactionCache.ordinal();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0: breedProgressTime = value;
                        break;
                    case 1: satisfactionCache = Satisfaction.of(value);
                        break;
                    case 2: weatherSatisfactionCache = Satisfaction.of(value);
                        break;
                    case 3: temperatureSatisfactionCache = Satisfaction.of(value);
                        break;
                    case 4: lightSatisfactionCache = Satisfaction.of(value);
                        break;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    public boolean continuous = false;
    public int breedProgressTime = 0;
    private Specie offspringCache = null;

    public ItemStack getDrone() {
        return inputItemHandler.getStackInSlot(0);
    }

    public ItemStack getPrincess() {
        return inputItemHandler.getStackInSlot(1);
    }

    public ItemStack getQueen() {
        return inputItemHandler.getStackInSlot(2);
    }

    private final ItemStackHandler inputItemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            checkLock = false;
            if(slot < 3) {
                if(getLevel().isClientSide())
                    return;
                breedCheck();
            }
            setChanged();
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if(stack.is(Index.DRONE_BEES_TAG) && slot == 0)
                return super.insertItem(slot, stack, simulate);
            if(stack.is(Index.PRINCESS_BEES_TAG) && slot == 1)
                return super.insertItem(slot, stack, simulate);
            if(stack.is(Index.QUEEN_BEES_TAG) && slot == 2)
                return super.insertItem(slot, stack, simulate);
            return stack;
        }
    };

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private void breedCheck() {
        ItemStack drone = getDrone();
        ItemStack princess = getPrincess();
        ItemStack queen = getQueen();

        if(drone.isEmpty())
            return;
        if(princess.isEmpty())
            return;
        if(!queen.isEmpty())
            return;

        if(drone.getTag() == null)
            BeeItem.init(drone);
        if(princess.getTag() == null)
            BeeItem.init(princess);

        offspringCache = BeeLifecycle.getOffspringSpecie(getLevel(), drone, princess);
    }

    private void preformBreeding() {
        if(offspringCache == null)
            return;

        ItemStack offspringQueen = new ItemStack(offspringCache.queenItem);
        offspringQueen.setTag(BeeLifecycle.getOffspringItemStack(getDrone(), getPrincess(), offspringCache));
        inputItemHandler.setStackInSlot(0, ItemStack.EMPTY);
        inputItemHandler.setStackInSlot(1, ItemStack.EMPTY);
        inputItemHandler.setStackInSlot(2, offspringQueen);
    }

    public void updateSatisfaction() {
        if(getQueen() == null || getQueen().isEmpty()) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        Specie specie = BeeItem.of(getQueen());
        if(specie == null) {
            satisfactionCache = Satisfaction.NOT_WORKING;
            return;
        }

        lightSatisfactionCache = specie.getLightSatisfaction(getQueen(), getLevel(), getBlockPos());
        weatherSatisfactionCache = specie.getWeatherSatisfaction(getQueen(), getLevel(), getBlockPos());
        temperatureSatisfactionCache = specie.getTemperatureSatisfaction(getQueen(), getLevel(), getBlockPos());

        satisfactionCache = Satisfaction.calc(lightSatisfactionCache, weatherSatisfactionCache, temperatureSatisfactionCache);
    }

    private LazyOptional<IItemHandler> lazyInputItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.empty();

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.beekeeping.apiary");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ApiaryMenu(id, inv, this, this.data);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(side == Direction.DOWN)
                return lazyOutputItemHandler.cast();
            return lazyInputItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyInputItemHandler = LazyOptional.of(() -> inputItemHandler);
        lazyOutputItemHandler = LazyOptional.of(() -> outputItemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyInputItemHandler.invalidate();
        lazyOutputItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("input", inputItemHandler.serializeNBT());
        tag.put("output", outputItemHandler.serializeNBT());
        tag.putBoolean("continuous", continuous);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        inputItemHandler.deserializeNBT(nbt.getCompound("input"));
        outputItemHandler.deserializeNBT(nbt.getCompound("output"));
        continuous = nbt.getBoolean("continuous");
    }

    public void drop() {
        SimpleContainer input = new SimpleContainer(inputItemHandler.getSlots());
        for (int i = 0; i < inputItemHandler.getSlots(); i++) {
            input.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, input);

        SimpleContainer output = new SimpleContainer(outputItemHandler.getSlots());
        for (int i = 0; i < outputItemHandler.getSlots(); i++) {
            output.setItem(i, outputItemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, output);
    }

    public int slowTick = 0;
    public boolean checkLock = false;
    public Satisfaction satisfactionCache;
    public Satisfaction weatherSatisfactionCache;
    public Satisfaction temperatureSatisfactionCache;
    public Satisfaction lightSatisfactionCache;
    public static void tick(Level level, BlockPos pos, BlockState state, ApiaryBlockEntity abe) {
        abe.localTick();
    }

    public void localTick() {
        if(getLevel().isClientSide())
            return;

        if(getQueen().isEmpty() && !getDrone().isEmpty() && !getPrincess().isEmpty()) {
            breedProgressTime++;
            if(breedProgressTime > BREED_TIME) {
                breedProgressTime = 0;
                preformBreeding();
            }
        }
        else
            breedProgressTime = 0;

        if(slowTick++ < 20)
            return;
        slowTick = 0;
        ItemStack queen = getQueen();
        Specie specie = BeeItem.of(queen);
        if(specie == null)
            return;
        if(queen.getTag() == null)
            BeeItem.init(queen);

        updateSatisfaction();

        int hp = BeeItem.getHealth(queen.getTag());
        if(hp <= 0) {
            if(checkLock)
                return;
            if(attemptInsert(level, queen, inputItemHandler, outputItemHandler, satisfactionCache == Satisfaction.SATISFIED, continuous)) {
                inputItemHandler.setStackInSlot(2, ItemStack.EMPTY);
                return;
            }
            checkLock = true;
            return;
        }
        if(satisfactionCache != Satisfaction.NOT_WORKING)
            BeeItem.setHealth(queen.getTag(), hp-LIFETIME_STEP);
    }

    public static boolean attemptInsert(Level level, ItemStack queen, ItemStackHandler input, ItemStackHandler output, boolean satisfied, boolean continuous) {
        var optional = BeeLifecycle.getProduceRecipe(level, queen);
        if(optional.isEmpty())
            return true;
        if(queen == null || queen.isEmpty())
            return true;
        BeeProduceRecipe bpr = optional.get();
        ItemStack commonProduce = bpr.getCommonProduce(satisfied);
        ItemStack rareProduce = bpr.getRolledRareProduce(satisfied, RareProduceGene.of(RareProduceGene.get(queen.getTag())).getChance());

        ItemStack princess = BeeLifecycle.clone(queen, bpr.getSpecie().princessItem);
        ItemStack drone = BeeLifecycle.clone(queen, bpr.getSpecie().droneItem);

        if(continuous) {
            if(!input.getStackInSlot(0).isEmpty())
                if(!insert(drone, output, true))
                    return false;
            if(!input.getStackInSlot(1).isEmpty())
                if(!insert(princess, output, true))
                    return false;

            if(!insert(commonProduce, output, true))
                return false;
            if(!insert(rareProduce, output, true))
                return false;

            if(input.getStackInSlot(0).isEmpty())
                input.setStackInSlot(0, drone);
            else
                insert(drone, output, false);

            if(input.getStackInSlot(1).isEmpty())
                input.setStackInSlot(1, princess);
            else
                insert(princess, output, false);
        }
        else {
            if(!insert(princess, output, true))
                return false;
            if(!insert(drone, output, true))
                return false;
            if(!insert(commonProduce, output, true))
                return false;
            if(!insert(rareProduce, output, true))
                return false;

            insert(princess, output, false);
            insert(drone, output, false);
        }
        insert(commonProduce, output, false);
        insert(rareProduce, output, false);
        return true;
    }

    public static boolean insert(ItemStack stack, ItemStackHandler output, boolean sim) {
        if(stack.isEmpty())
            return true;
        for(int i = 0; i < output.getSlots(); i++) {
            stack = output.insertItem(i, stack, sim);
            if(stack.isEmpty())
                return true;
        }
        return stack.isEmpty();
    }

    @Override
    public void onToggle(ServerPlayer player, int index, boolean value) {
        switch(index) {
            case 0:
                continuous = value;
                setChanged();
                break;
        }
        if(player != null)
            TogglePacket.sync(getBlockPos(), getLevel(), index, value);
    }
}