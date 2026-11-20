package ec.brooke.minting.data;

import ec.brooke.minting.Minting;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Minting.MODID)
public class MintingDataMaps {
    public static final DataMapType<Item, CoinMaterial> COIN_MATERIALS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(Minting.MODID, "coin_materials"),
            Registries.ITEM,
            CoinMaterial.CODEC
    ).synced(CoinMaterial.CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(COIN_MATERIALS);
    }
}
