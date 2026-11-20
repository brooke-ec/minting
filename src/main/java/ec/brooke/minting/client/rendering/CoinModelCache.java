package ec.brooke.minting.client.rendering;

import ec.brooke.minting.Minting;
import ec.brooke.minting.items.components.CoinDesign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoinModelCache {
    private static final int MAX_CAPACITY = 256;
    private static final Map<CoinDesign, BakedModel> CACHE = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CoinDesign, BakedModel> eldest) {
            if (size() <= MAX_CAPACITY) return false;
            Minecraft.getInstance().getTextureManager().release(idOf(eldest.getKey()));
            return true;
        }
    };
    private static BakedModel template;

    private CoinModelCache() {
    }

    public static void setTemplate(BakedModel t) {
        template = t;
        clear();
    }

    public static void clear() {
        var manager = Minecraft.getInstance().getTextureManager();
        CACHE.keySet().forEach(d -> manager.release(idOf(d)));
        CACHE.clear();
    }

    public static BakedModel get(CoinDesign design) {
        BakedModel model = CACHE.get(design);

        if (model == null) {
            model = CoinModel.build(idOf(design), CoinTexture.build(design), template);
            CACHE.put(design, model);
        }

        return model;
    }

    private static ResourceLocation idOf(CoinDesign design) {
        return ResourceLocation.fromNamespaceAndPath(Minting.MODID, "coin/" + design.hashCode());
    }
}
