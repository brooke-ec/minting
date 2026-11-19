package ec.brooke.minting.blocks;

import com.mojang.serialization.MapCodec;
import ec.brooke.minting.Minting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MintingBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Minting.MODID);
    public static final DeferredRegister<MapCodec<? extends Block>> BLOCK_CODECS = DeferredRegister.create(
            BuiltInRegistries.BLOCK_TYPE, Minting.MODID);


    public static final DeferredBlock<EngravingTableBlock> ENGRAVING_TABLE = BLOCKS.registerBlock("engraving_table",
            registryName -> new EngravingTableBlock(BlockBehaviour.Properties.of().destroyTime(2.0f).explosionResistance(10.0f)
                    .sound(SoundType.WOOD))
    );

}
