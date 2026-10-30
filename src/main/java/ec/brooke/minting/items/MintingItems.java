package ec.brooke.minting.items;

import ec.brooke.minting.Minting;
import ec.brooke.minting.blocks.MintingBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MintingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Minting.MODID);

    public static final DeferredItem<BlockItem> ENGRAVING_TABLE = ITEMS.registerSimpleBlockItem("engraving_table",
            MintingBlocks.ENGRAVING_TABLE
    );
}
