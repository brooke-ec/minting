package ec.brooke.minting.items.components;

import ec.brooke.minting.Minting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MintingComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Minting.MODID);

    public static final Supplier<DataComponentType<CoinShape>> COIN_SHAPE = COMPONENTS.registerComponentType("shape",
            builder -> builder.persistent(CoinShape.CODEC).networkSynchronized(CoinShape.STREAM_CODEC)
    );
}
