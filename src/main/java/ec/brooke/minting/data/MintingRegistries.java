package ec.brooke.minting.data;

import ec.brooke.minting.Minting;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Minting.MODID)
public class MintingRegistries {
    public static final ResourceKey<Registry<CoinMaterial>> COIN_MATERIAL = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath("minting", "material"));


    @SubscribeEvent
    static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(COIN_MATERIAL, CoinMaterial.CODEC, CoinMaterial.CODEC);
    }
}
