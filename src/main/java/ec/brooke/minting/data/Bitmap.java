package ec.brooke.minting.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.Base64;

public class Bitmap implements Cloneable {
    public static final Bitmap DEFAULT = new Bitmap(Base64.getDecoder().decode("AAAAAAAAAABVBZRvQf4bpKoBVQUAAAAAAA=="));
    public static final int DEPTH = 2; // 2 bits per pixel
    public static final int SIZE = 10; // 10x10 pixels
    private static final int LENGTH = (DEPTH * SIZE * SIZE) / 8;
    public static final Codec<Bitmap> CODEC = Codec.STRING.comapFlatMap(buf -> {
        byte[] bytes = Base64.getDecoder().decode(buf);
        if (bytes.length != LENGTH) return DataResult.error(() -> "Bitmap must be exactly " + LENGTH + " bytes long");
        return DataResult.success(new Bitmap(bytes));
    }, mask -> Base64.getEncoder().encodeToString(mask.bytes));
    public static final StreamCodec<ByteBuf, Bitmap> STREAM_CODEC = StreamCodec.of(
            (buffer, bitmap) -> buffer.writeBytes(bitmap.bytes),
            buffer -> {
                byte[] bytes = new byte[LENGTH];
                buffer.readBytes(bytes);
                return new Bitmap(bytes);
            }
    );
    private final byte[] bytes;

    public Bitmap() {
        this.bytes = new byte[LENGTH];
        fill();
    }

    private Bitmap(byte[] bytes) {
        this.bytes = bytes;
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

        return (bytes[byteIndex] >> bitOffset) & 0b11;
    }

    public void set(int x, int y, int value) {
        checkBounds(x, y);

        if (value < 0 || value > 3) throw new IllegalArgumentException("Value must be between 0 and 3, not " + value);

        int index = (y * 10 + x) * 2;
        int byteIndex = index / 8;
        int bitOffset = index % 8;

        bytes[byteIndex] &= (byte) ~(0b11 << bitOffset);
        bytes[byteIndex] |= (byte) ((value & 0b11) << bitOffset);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bitmap other && Arrays.equals(bytes, other.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    public void clear() {
        Arrays.fill(bytes, (byte) 0);
    }

    public void fill() {
        Arrays.fill(bytes, (byte) 0xFF);
    }

    @Override
    public Bitmap clone() {
        return new Bitmap(bytes.clone());
    }

    public void invert() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int value = get(x, y);
                set(x, y, 3 - value);
            }
        }
    }
}
