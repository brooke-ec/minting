package ec.brooke.minting.items.components;

import com.mojang.serialization.Codec;
import ec.brooke.minting.Bitmap;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record CoinShape(Bitmap bitmap) {
    public static final Codec<CoinShape> CODEC = Bitmap.CODEC.xmap(CoinShape::new, CoinShape::bitmap);
    public static final StreamCodec<ByteBuf, CoinShape> STREAM_CODEC = Bitmap.STREAM_CODEC.map(CoinShape::new, CoinShape::bitmap);
}
