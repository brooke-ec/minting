package ec.brooke.minting.items.components;

import ec.brooke.minting.Minting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MintingComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Minting.MODID);

    public static final Supplier<DataComponentType<CoinDesign>> COIN_DESIGN = COMPONENTS.registerComponentType("design",
            builder -> builder.persistent(CoinDesign.CODEC).networkSynchronized(CoinDesign.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<DieShape>> DIE_SHAPE = COMPONENTS.registerComponentType("shape",
            builder -> builder.persistent(DieShape.CODEC).networkSynchronized(DieShape.STREAM_CODEC)
    );
}
