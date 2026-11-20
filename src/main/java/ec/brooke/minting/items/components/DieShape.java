package ec.brooke.minting.items.components;

import com.mojang.serialization.Codec;
import ec.brooke.minting.data.Bitmap;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record DieShape(Bitmap bitmap) {
    public static final Codec<DieShape> CODEC = Bitmap.CODEC.xmap(DieShape::new, DieShape::bitmap);
    public static final StreamCodec<ByteBuf, DieShape> STREAM_CODEC = Bitmap.STREAM_CODEC.map(DieShape::new, DieShape::bitmap);
}
