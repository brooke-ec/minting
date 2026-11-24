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

    public static final DeferredItem<BlockItem> COINING_PRESS = ITEMS.registerSimpleBlockItem("coining_press",
            MintingBlocks.COINING_PRESS
    );

    public static final DeferredItem<Item> STRIKING_DIE = ITEMS.registerSimpleItem("striking_die");

    public static final DeferredItem<Item> COIN = ITEMS.register("coin", () -> new CoinItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.registerSimpleItem("copper_nugget");

    public static final DeferredItem<Item> NETHERITE_NUGGET = ITEMS.registerSimpleItem("netherite_nugget");

    @SubscribeEvent
    public static void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ENGRAVING_TABLE);
            event.accept(COINING_PRESS);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(NETHERITE_NUGGET);
            event.accept(COPPER_NUGGET);
        }
    }
}
