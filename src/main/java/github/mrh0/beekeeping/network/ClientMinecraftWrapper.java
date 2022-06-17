package github.mrh0.beekeeping.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ClientMinecraftWrapper {
    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }
}
