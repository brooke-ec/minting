package ec.brooke.minting.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import ec.brooke.minting.items.components.CoinDesign;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class CoinTexture {
    private static final ResourceLocation BASE_PATH =
            ResourceLocation.fromNamespaceAndPath("minting", "textures/item/gold_coin.png");

    private CoinTexture() {
    }

    public static NativeImage build(CoinDesign coin) {
        NativeImage image = loadBase();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (x < 3 || x >= 13 || y < 3 || y >= 13) image.setPixelRGBA(x, y, 0x00000000);
                else if (coin.bitmap().get(x - 3, y - 3) == 3) image.setPixelRGBA(x, y, 0x00000000);
            }
        }

        return image;
    }

    private static NativeImage loadBase() {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();
        try (InputStream in = rm.getResourceOrThrow(BASE_PATH).open()) {
            return NativeImage.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load " + BASE_PATH, e);
        }
    }
}
