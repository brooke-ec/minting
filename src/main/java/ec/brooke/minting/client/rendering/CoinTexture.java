package ec.brooke.minting.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import ec.brooke.minting.items.components.CoinDesign;

public class CoinTexture {
    private CoinTexture() {
    }

    public static NativeImage build(CoinDesign coin) {
        NativeImage image = CoinCache.getMaterial(coin.material());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (x < 3 || x >= 13 || y < 3 || y >= 13) image.setPixelRGBA(x, y, 0x00000000);
                else if (coin.bitmap().get(x - 3, y - 3) == 3) image.setPixelRGBA(x, y, 0x00000000);
            }
        }

        return image;
    }
}
