package ec.brooke.minting.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ec.brooke.minting.Minting;
import net.minecraft.resources.ResourceLocation;

public record CoinMaterial(ResourceLocation texture, String translationKey) {
    public static final Codec<CoinMaterial> CODEC = RecordCodecBuilder.create(insance -> insance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(CoinMaterial::texture),
            Codec.STRING.fieldOf("translation_key").forGetter(CoinMaterial::translationKey)
    ).apply(insance, CoinMaterial::new));

    public static final CoinMaterial FALLBACK = new CoinMaterial(ResourceLocation.fromNamespaceAndPath(Minting.MODID, "item/unknown"),
            "material.minting.unknown"
    );
}
