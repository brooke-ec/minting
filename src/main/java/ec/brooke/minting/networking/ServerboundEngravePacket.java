package ec.brooke.minting.networking;

import ec.brooke.minting.Minting;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.menus.EngravingMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ServerboundEngravePacket(Bitmap bitmap) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundEngravePacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Minting.MODID, "submit_bitmap"));

    public static final StreamCodec<ByteBuf, ServerboundEngravePacket> STREAM_CODEC = Bitmap.STREAM_CODEC.map(ServerboundEngravePacket::new,
            ServerboundEngravePacket::bitmap
    );

    public static void handle(final ServerboundEngravePacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player && player.containerMenu instanceof EngravingMenu menu && menu.stillValid(player))
                menu.setBitmap(packet.bitmap());
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
