package ec.brooke.minting.networking;

import ec.brooke.minting.Minting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Minting.MODID)
public class MintingPackets {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                ServerboundEngravePacket.TYPE,
                ServerboundEngravePacket.STREAM_CODEC,
                ServerboundEngravePacket::handle
        );
    }
}
