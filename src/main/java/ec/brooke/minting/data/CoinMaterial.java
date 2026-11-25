package ec.brooke.minting.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ec.brooke.minting.Minting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber
public record CoinMaterial(@NotNull Ingredient ingredient, @NotNull ResourceLocation texture, @NotNull Component name, int dark, int light) {

    public static final Codec<CoinMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CoinMaterial::ingredient),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(CoinMaterial::texture),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(CoinMaterial::name),
            Codec.INT.fieldOf("dark").forGetter(CoinMaterial::dark),
            Codec.INT.fieldOf("light").forGetter(CoinMaterial::light)
    ).apply(instance, CoinMaterial::new));

    public static final CoinMaterial DEFAULT = new CoinMaterial(
            Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Minting.MODID, "unknown_material"))),
            ResourceLocation.fromNamespaceAndPath(Minting.MODID, "item/coin/unknown"),
            Component.translatable("item.minting.coin.unknown"),
            0x5e0909, 0x81bbeb
    );

    private static final int CACHE_MAX_SIZE = 256;

    private static final Map<Item, Optional<Holder.Reference<CoinMaterial>>> RESOLVE_CACHE = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Item, Optional<Holder.Reference<CoinMaterial>>> eldest) {
            return size() > CACHE_MAX_SIZE;
        }
    };

    public static Optional<Holder.Reference<CoinMaterial>> resolve(HolderLookup.Provider registries, ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();
        return RESOLVE_CACHE.computeIfAbsent(stack.getItem(), item -> registries.lookupOrThrow(MintingRegistries.COIN_MATERIAL)
                .listElements().filter(h -> h.value().ingredient().test(item.getDefaultInstance()))
                .min(Comparator.comparing(h -> h.key().location()))
        );
    }

    @SubscribeEvent
    public static void datapackSync(TagsUpdatedEvent event) {
        RESOLVE_CACHE.clear();
    }

    @SubscribeEvent
    static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        RESOLVE_CACHE.clear();
    }
}
