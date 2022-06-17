package github.mrh0.beekeeping.network;

import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public interface IHasFloatOption {
    public void onFloat(@Nullable ServerPlayer player, int index, float value);
}
