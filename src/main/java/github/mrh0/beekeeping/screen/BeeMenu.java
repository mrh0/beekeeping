package github.mrh0.beekeeping.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BeeMenu <E extends BlockEntity> extends AbstractContainerMenu {
    protected BeeMenu(@Nullable MenuType<?> menuType, int id) {
        super(menuType, id);
    }

    public abstract E getBlockEntity();
}
