package ec.brooke.minting.items.components;

import com.mojang.serialization.Codec;
import ec.brooke.minting.Minting;
import ec.brooke.minting.data.Bitmap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MintingComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Minting.MODID);

    public static final Supplier<DataComponentType<CoinDesign>> COIN_DESIGN = COMPONENTS.registerComponentType("design",
            builder -> builder.persistent(CoinDesign.CODEC).networkSynchronized(CoinDesign.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<Bitmap>> DIE_SHAPE = COMPONENTS.registerComponentType("shape",
            builder -> builder.persistent(Bitmap.CODEC).networkSynchronized(Bitmap.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<String>> MINTER = COMPONENTS.registerComponentType("minter",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );
}
