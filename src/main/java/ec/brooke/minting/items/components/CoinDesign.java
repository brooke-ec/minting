package ec.brooke.minting.items.components;

import com.mojang.serialization.Codec;
import ec.brooke.minting.Bitmap;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record CoinDesign(Bitmap bitmap) {
    public static final Codec<CoinDesign> CODEC = Bitmap.CODEC.xmap(CoinDesign::new, CoinDesign::bitmap);
    public static final StreamCodec<ByteBuf, CoinDesign> STREAM_CODEC = Bitmap.STREAM_CODEC.map(CoinDesign::new, CoinDesign::bitmap);
}
