package ec.brooke.minting.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.data.CoinMaterial;
import ec.brooke.minting.data.MintingDataMaps;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record CoinDesign(Bitmap bitmap, ResourceLocation item) {
    public static final CoinDesign FALLBACK = new CoinDesign(Bitmap.DEFAULT, null);
    public static final Codec<CoinDesign> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Bitmap.CODEC.fieldOf("shape").forGetter(CoinDesign::bitmap),
            ResourceLocation.CODEC.fieldOf("material").forGetter(CoinDesign::item)
    ).apply(instance, CoinDesign::new));

    public static final StreamCodec<ByteBuf, CoinDesign> STREAM_CODEC = StreamCodec.composite(
            Bitmap.STREAM_CODEC, CoinDesign::bitmap,
            ResourceLocation.STREAM_CODEC, CoinDesign::item,
            CoinDesign::new
    );

    public CoinMaterial material() {
        return BuiltInRegistries.ITEM.getHolder(item)
                .map(holder -> holder.getData(MintingDataMaps.COIN_MATERIALS)).orElse(CoinMaterial.FALLBACK);
    }
}