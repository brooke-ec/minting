package ec.brooke.minting;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import static ec.brooke.minting.blocks.MintingBlocks.BLOCKS;
import static ec.brooke.minting.blocks.MintingBlocks.BLOCK_CODECS;
import static ec.brooke.minting.items.MintingItems.ITEMS;
import static ec.brooke.minting.menus.MintingMenus.MENUS;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Minting.MODID)
public class Minting {
    public static final String MODID = "minting";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Minting(IEventBus modEventBus, ModContainer modContainer) {
        BLOCK_CODECS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
    }
}
