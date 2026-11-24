package ec.brooke.minting.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import ec.brooke.minting.Minting;
import ec.brooke.minting.data.CoinMaterial;
import ec.brooke.minting.data.MintingRegistries;
import ec.brooke.minting.items.components.CoinDesign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@EventBusSubscriber(value = Dist.CLIENT, modid = Minting.MODID)
public class CoinCache {
    private static final int MAX_CAPACITY = 256;
    private static final Map<CoinDesign, BakedModel> MODEL_CACHE = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CoinDesign, BakedModel> eldest) {
            if (size() <= MAX_CAPACITY) return false;
            Minecraft.getInstance().getTextureManager().release(idOf(eldest.getKey()));
            return true;
        }
    };
    private static final Map<CoinMaterial, NativeImage> MATERIAL_CACHE = new LinkedHashMap<>(16, 0.75f, true);
    private static NativeImage unknownMaterial;
    private static BakedModel template;

    private CoinCache() {
    }

    public static void setTemplate(BakedModel t) {
        template = t;
        reset();
    }

    public static void reset() {
        Minecraft minecraft = Minecraft.getInstance();

        TextureManager manager = minecraft.getTextureManager();
        ClientLevel level = minecraft.level;
        if (level == null) return;

        MATERIAL_CACHE.keySet().forEach(m -> manager.release(m.texture()));
        MATERIAL_CACHE.clear();

        // load all coin materials into the cache
        unknownMaterial = loadTexture(CoinMaterial.DEFAULT.texture());
        MATERIAL_CACHE.putAll(level.registryAccess()
                .lookup(MintingRegistries.COIN_MATERIAL)
                .stream().flatMap(HolderLookup::listElements)
                .map(Holder.Reference::value)
                .map(h -> Map.entry(h, loadTexture(h.texture())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );

        MODEL_CACHE.keySet().forEach(d -> manager.release(idOf(d)));
        MODEL_CACHE.clear();
    }

    public static BakedModel getModel(CoinDesign design) {
        BakedModel model = MODEL_CACHE.get(design);

        if (model == null) {
            model = CoinModel.build(idOf(design), CoinTexture.build(design), template);
            MODEL_CACHE.put(design, model);
        }

        return model;
    }

    private static ResourceLocation idOf(CoinDesign design) {
        return ResourceLocation.fromNamespaceAndPath(Minting.MODID, "coin/" + design.hashCode());
    }

    public static @NotNull NativeImage getMaterial(CoinMaterial material) {
        NativeImage image = MATERIAL_CACHE.get(material);
        if (image == null) image = Objects.requireNonNull(unknownMaterial);

        NativeImage copy = new NativeImage(image.getWidth(), image.getHeight(), false);
        copy.copyFrom(image);
        return copy;
    }

    private static @NotNull NativeImage loadTexture(ResourceLocation path) {
        path = ResourceLocation.fromNamespaceAndPath(path.getNamespace(), "textures/" + path.getPath() + ".png");
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        Optional<Resource> resource = rm.getResource(path);
        if (resource.isPresent()) {
            try (InputStream in = resource.get().open()) {
                return NativeImage.read(in);
            } catch (IOException e) {
                Minting.LOGGER.error("Failed to read coin texture {}", path, e);
            }
        }

        Minting.LOGGER.error("Missing coin texture {}", path);
        return Objects.requireNonNull(MissingTextureAtlasSprite.getTexture().getPixels());
    }

    @SubscribeEvent
    public static void datapackSync(OnDatapackSyncEvent event) {
        reset();
    }

    @SubscribeEvent
    static void onRegister(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) rm -> reset());
    }

    @SubscribeEvent
    static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        reset();
    }
}
