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

public class FloatPacket {
    private BlockPos pos;
    private int index;
    private float value;

    public FloatPacket(BlockPos pos, int index, float value) {
        this.pos = pos;
        this.index = index;
        this.value = value;
    }

    public static void encode(FloatPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeInt(packet.index);
        buf.writeFloat(packet.value);
    }

    public static FloatPacket decode(FriendlyByteBuf buf) {
        return new FloatPacket(buf.readBlockPos(), buf.readInt(), buf.readFloat());
    }

    public static void handle(FloatPacket pkt, Supplier<NetworkEvent.Context> ctx) {
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

    private static void sendUpdateServer(FloatPacket pkt, ServerPlayer player) {
        BlockEntity te = player.level.getBlockEntity(pkt.pos);
        if (te != null) {
            if (te instanceof IHasFloatOption) {
                IHasFloatOption ote = (IHasFloatOption) te;
                ote.onFloat(player, pkt.index, pkt.value);
                Packet<ClientGamePacketListener> supdatetileentitypacket = te.getUpdatePacket();
                if (supdatetileentitypacket != null)
                    player.connection.send(supdatetileentitypacket);
            }
        }
    }

    private static void sendUpdateClient(FloatPacket pkt) {
        BlockEntity te = ClientMinecraftWrapper.getClientLevel().getBlockEntity(pkt.pos);
        if (te != null) {
            if(te instanceof IHasFloatOption) {
                IHasFloatOption to = (IHasFloatOption) te;
                to.onFloat(null, pkt.index, pkt.value);
            }
        }
    }

    public static void send(BlockPos pos, Level level, int index, float value) {
        Beekeeping.NETWORK.sendToServer(new FloatPacket(pos, index, value));
    }

    public static void sync(BlockPos pos, Level level, int index, float value) {
        Beekeeping.NETWORK.send(PacketDistributor.DIMENSION.with(() -> level.dimension()), new FloatPacket(pos, index, value));
    }
}