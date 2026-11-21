package ec.brooke.minting.client;

import ec.brooke.minting.Minting;
import ec.brooke.minting.client.screens.CoiningScreen;
import ec.brooke.minting.client.screens.EngravingScreen;
import ec.brooke.minting.menus.MintingMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = Minting.MODID)
@Mod(value = Minting.MODID, dist = Dist.CLIENT)
public class MintingClient {
    public MintingClient(IEventBus modBus) {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MintingMenus.ENGRAVING.get(), EngravingScreen::new);
        event.register(MintingMenus.COINING.get(), CoiningScreen::new);
    }
}
