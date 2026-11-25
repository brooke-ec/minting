package ec.brooke.minting.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ec.brooke.minting.Minting;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.data.CoinMaterial;
import ec.brooke.minting.data.MintingRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CoinDesign(@NotNull ResourceKey<CoinMaterial> materialKey, @NotNull Bitmap bitmap) {
    public static final CoinDesign DEFAULT = new CoinDesign(
            ResourceKey.create(MintingRegistries.COIN_MATERIAL, ResourceLocation.fromNamespaceAndPath(Minting.MODID, "unknown")), Bitmap.DEFAULT);
    public static final Codec<CoinDesign> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(MintingRegistries.COIN_MATERIAL).fieldOf("material").forGetter(CoinDesign::materialKey),
            Bitmap.CODEC.fieldOf("shape").forGetter(CoinDesign::bitmap)
    ).apply(instance, CoinDesign::new));

    public static final StreamCodec<ByteBuf, CoinDesign> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(MintingRegistries.COIN_MATERIAL), CoinDesign::materialKey,
            Bitmap.STREAM_CODEC, CoinDesign::bitmap,
            CoinDesign::new
    );

    public CoinMaterial material(HolderLookup.Provider registries) {
        return registries.lookup(MintingRegistries.COIN_MATERIAL)
                .flatMap(l -> l.get(materialKey))
                .map(Holder::value)
                .orElse(CoinMaterial.DEFAULT);
    }
}