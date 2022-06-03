package github.mrh0.beekeeping.blocks.apiary;

import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.breeding.BeeLifecycle;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import github.mrh0.beekeeping.screen.apiary.ApiaryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
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

public class ApiaryBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    public ApiaryBlockEntity(BlockPos pos, BlockState state) {
        super(Index.APIARY_BLOCK_ENTITY.get(), pos, state);
        data = new SimpleContainerData(2);
    }

    private ItemStack getDrone() {
        return itemHandler.getStackInSlot(0);
    }

    private ItemStack getPrincess() {
        return itemHandler.getStackInSlot(1);
    }

    private ItemStack getQueen() {
        return itemHandler.getStackInSlot(2);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            checkLock = false;
            if(slot < 3) {
                if(getLevel().isClientSide())
                    return;
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

                Specie offspring = BeeLifecycle.getOffspringSpecie(getLevel(), drone, princess);
                if(offspring == null)
                    return;
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                itemHandler.setStackInSlot(1, ItemStack.EMPTY);
                itemHandler.setStackInSlot(2, BeeLifecycle.getOffspringItemStack(drone, princess, offspring));
            }
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyItemHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drop() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public int slowTick = 0;
    public boolean checkLock = false;
    public static void tick(Level level, BlockPos pos, BlockState state, ApiaryBlockEntity abe) {
        if(abe.slowTick++ < 20)
            return;
        abe.slowTick = 0;
        ItemStack queen = abe.getQueen();
        Specie specie = BeeItem.of(queen);
        if(specie == null)
            return;
        if(queen.getTag() == null)
            return;
        int hp = BeeItem.getHealth(queen.getTag());
        if(hp <= 0) {
            if(abe.checkLock)
                return;
            if(attemptInsert(level, queen, abe.itemHandler)) {
                abe.itemHandler.setStackInSlot(2, ItemStack.EMPTY);
                return;
            }
            abe.checkLock = true;
            return;
        }
        BeeItem.setHealth(queen.getTag(), hp-100);
    }

    public static boolean attemptInsert(Level level, ItemStack queen, ItemStackHandler inv) {
        var optional = BeeLifecycle.getProduceRecipe(level, queen);
        if(optional.isEmpty())
            return true;
        BeeProduceRecipe bpr = optional.get();
        ItemStack commonProduce = bpr.getCommonProduce();
        ItemStack rareProduce = bpr.getRolledRareProduce();
        if(!insert(commonProduce, inv, true))
            return false;
        if(!insert(rareProduce, inv, true))
            return false;
        insert(commonProduce, inv, false);
        insert(rareProduce, inv, false);
        return true;
    }

    public static boolean insert(ItemStack stack, ItemStackHandler inv, boolean sim) {
        if(stack.isEmpty())
            return true;
        for(int i = 3; i < inv.getSlots(); i++) {
            stack = inv.insertItem(i, stack, sim);
            if(stack.isEmpty())
                return true;
        }
        return stack.isEmpty();
    }
}