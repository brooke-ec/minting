package ec.brooke.minting.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import ec.brooke.minting.data.Bitmap;
import ec.brooke.minting.items.components.CoinDesign;
import net.minecraft.util.FastColor;

public class CoinTexture {
    private static final int OFFSET = 3;
    private static final float BITMAP_1 = 0.30f;
    private static final float BITMAP_2 = 0.85f;
    private static final float BEVEL_DARK = 0.20f;
    private static final float BEVEL_LIGHT = 0.45f;

    private CoinTexture() {
    }

    public static NativeImage build(CoinDesign coin) {
        NativeImage image = CoinCache.getMaterial(coin.material());
        int dark = FastColor.ARGB32.opaque(coin.material().dark());
        int light = FastColor.ARGB32.opaque(coin.material().light());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int px = image.getPixelRGBA(x, y);

                if (x < OFFSET || x >= OFFSET + Bitmap.SIZE || y < OFFSET || y >= OFFSET + Bitmap.SIZE) px = 0;
                else {
                    switch (coin.bitmap().get(x - OFFSET, y - OFFSET)) {
                        case 1 -> px = mix(px, dark, BITMAP_1);
                        case 2 -> px = mix(px, dark, BITMAP_2);
                        case 3 -> px = 0;
                    }
                }

                image.setPixelRGBA(x, y, px);

                if (isTransparent(px)) {
                    shade(image, x - 1, y, dark, BEVEL_DARK);
                    shade(image, x, y - 1, dark, BEVEL_DARK);
                    shade(image, x, y + 1, light, BEVEL_LIGHT);
                    shade(image, x + 1, y, light, BEVEL_LIGHT);
                }
            }
        }
        return image;
    }

    private static boolean isTransparent(int abgr) {
        return FastColor.ABGR32.alpha(abgr) == 0;
    }

    private static void shade(NativeImage image, int x, int y, int tintArgb, float strength) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) return;
        int px = image.getPixelRGBA(x, y);
        if (!isTransparent(px)) image.setPixelRGBA(x, y, mix(px, tintArgb, strength));
    }

    private static int mix(int abgr, int tintArgb, float t) {
        return FastColor.ABGR32.fromArgb32(FastColor.ARGB32.lerp(t, FastColor.ABGR32.fromArgb32(abgr), tintArgb));
    }
}
