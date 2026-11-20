package ec.brooke.minting.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ec.brooke.minting.Minting;
import net.minecraft.resources.ResourceLocation;

public record CoinMaterial(ResourceLocation texture, String translationKey, int dark, int light) {
    public static final Codec<CoinMaterial> CODEC = RecordCodecBuilder.create(insance -> insance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(CoinMaterial::texture),
            Codec.STRING.fieldOf("translation_key").forGetter(CoinMaterial::translationKey),
            Codec.INT.fieldOf("dark").forGetter(CoinMaterial::dark),
            Codec.INT.fieldOf("light").forGetter(CoinMaterial::light)
    ).apply(insance, CoinMaterial::new));

    public static final CoinMaterial FALLBACK = new CoinMaterial(ResourceLocation.fromNamespaceAndPath(Minting.MODID, "item/unknown"),
            "material.minting.unknown", 0x5e0909, 0x81bbeb
    );
}
