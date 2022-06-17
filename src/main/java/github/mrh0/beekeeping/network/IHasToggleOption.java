package github.mrh0.beekeeping.network;

import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public interface IHasToggleOption {
    public void onToggle(@Nullable ServerPlayer player, int index, boolean value);
}
