package ec.brooke.minting.items;

import ec.brooke.minting.Minting;
import ec.brooke.minting.blocks.MintingBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Minting.MODID)
public class MintingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Minting.MODID);

    public static final DeferredItem<BlockItem> ENGRAVING_TABLE = ITEMS.registerSimpleBlockItem("engraving_table",
            MintingBlocks.ENGRAVING_TABLE
    );

    public static final DeferredItem<Item> STRIKING_DIE = ITEMS.registerSimpleItem("striking_die");

    @SubscribeEvent
    public static void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ENGRAVING_TABLE);
        }
    }
}
