package ec.brooke.minting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Bitmap {
    public static final int DEPTH = 2; // 2 bits per pixel
    private static final int LENGTH = (DEPTH * 10 * 10) / 8;
    public static final Codec<Bitmap> CODEC = Codec.BYTE_BUFFER.comapFlatMap(buf -> {
        if (buf.remaining() != LENGTH) return DataResult.error(() -> "PixelMask must be exactly " + LENGTH + " bytes long");
        byte[] bits = new byte[LENGTH];
        buf.get(bits);
        return DataResult.success(new Bitmap(bits));
    }, mask -> ByteBuffer.wrap(mask.bits));
    private final byte[] bits;

    public Bitmap() {
        this.bits = new byte[LENGTH];
    }

    private Bitmap(byte[] bits) {
        this.bits = bits;
    }

    private void checkBounds(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
    }

    public int get(int x, int y) {
        checkBounds(x, y);

        int index = (y * 10 + x) * 2;
        int byteIndex = index / 8;
        int bitOffset = index % 8;

        return (bits[byteIndex] >> bitOffset) & 0b11;
    }

    public void set(int x, int y, int value) {
        checkBounds(x, y);

        if (value < 0 || value > 3) throw new IllegalArgumentException("Value must be between 0 and 3, not " + value);

        int index = (y * 10 + x) * 2;
        int byteIndex = index / 8;
        int bitOffset = index % 8;

        bits[byteIndex] &= (byte) ~(0b11 << bitOffset);
        bits[byteIndex] |= (byte) ((value & 0b11) << bitOffset);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bitmap other && Arrays.equals(bits, other.bits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }
}
