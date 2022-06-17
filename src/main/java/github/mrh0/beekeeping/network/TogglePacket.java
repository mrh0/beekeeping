package github.mrh0.beekeeping.network;

import github.mrh0.beekeeping.Beekeeping;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class TogglePacket {
    private BlockPos pos;
    private int index;
    private boolean value;

    public TogglePacket(BlockPos pos, int index, boolean value) {
        this.pos = pos;
        this.index = index;
        this.value = value;
    }

    public static void encode(TogglePacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeInt(packet.index);
        buf.writeBoolean(packet.value);
    }

    public static TogglePacket decode(FriendlyByteBuf buf) {
        return new TogglePacket(buf.readBlockPos(), buf.readInt(), buf.readBoolean());
    }

    public static void handle(TogglePacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                    ServerPlayer player = ctx.get().getSender();
                    if (player != null)
                        sendUpdateServer(pkt, player);
                }
                else {
                    sendUpdateClient(pkt);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ctx.get().setPacketHandled(true);
    }

    private static void sendUpdateServer(TogglePacket pkt, ServerPlayer player) {
        BlockEntity te = player.level.getBlockEntity(pkt.pos);
        if (te != null) {
            if (te instanceof IHasToggleOption) {
                IHasToggleOption ote = (IHasToggleOption) te;
                ote.onToggle(player, pkt.index, pkt.value);
                Packet<ClientGamePacketListener> supdatetileentitypacket = te.getUpdatePacket();
                if (supdatetileentitypacket != null)
                    player.connection.send(supdatetileentitypacket);
            }
        }
    }

    private static void sendUpdateClient(TogglePacket pkt) {
        BlockEntity te = ClientMinecraftWrapper.getClientLevel().getBlockEntity(pkt.pos);
        if (te != null) {
            if(te instanceof IHasToggleOption) {
                IHasToggleOption to = (IHasToggleOption) te;
                to.onToggle(null, pkt.index, pkt.value);
            }
        }
    }

    public static void send(BlockPos pos, Level level, int index, boolean value) {
        Beekeeping.NETWORK.sendToServer(new TogglePacket(pos, index, value));
    }

    public static void sync(BlockPos pos, Level level, int index, boolean value) {
        Beekeeping.NETWORK.send(PacketDistributor.DIMENSION.with(() -> level.dimension()), new TogglePacket(pos, index, value));
    }
}