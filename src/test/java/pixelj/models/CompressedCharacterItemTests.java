package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import pixelj.graphics.BinaryImage;
import pixelj.models.CompressedGlyph.MisshapenDataException;

public class CompressedCharacterItemTests {

    /** Compress-decompress a square image. */
    @Test
    public void testSquare() {
        final var size = 16;
        final var capitalA = new Glyph(65, 10, BinaryImage.of(size, size, false));
        for (var i = 0; i < size; i++) {
            capitalA.getImage().set(i, i, true);
        }

        final var compressed = CompressedGlyph.from(capitalA);

        final var inBytes = new byte[size * size];
        final var outBytes = new byte[size * size];

        capitalA.getImage().getDataElements(0, 0, size, size, inBytes);
        try {
            final var decompressed = compressed.decompress(size, size);
            decompressed.getImage().getDataElements(0, 0, size, size, outBytes);
        } catch (MisshapenDataException exception) {
            throw new RuntimeException(exception);
        }

        assertArrayEquals(inBytes, outBytes);
    }

    /** Compress-decompress a rectangular image. */
    @Test
    public void testRectangular() {
        final var width = 8;
        final var height = 21;

        final var capitalA = new Glyph(65, 10, BinaryImage.of(width, height, false));
        final var min = Math.min(width, height);
        for (var i = 0; i < min; i++) {
            capitalA.getImage().set(i, i, true);
        }

        final var compressed = CompressedGlyph.from(capitalA);

        final var inBytes = new byte[width * height];
        final var outBytes = new byte[width * height];

        capitalA.getImage().getDataElements(0, 0, width, height, inBytes);
        try {
            final var decompressed = compressed.decompress(width, height);
            decompressed.getImage().getDataElements(0, 0, width, height, outBytes);
        } catch (MisshapenDataException exception) {
            throw new RuntimeException(exception);
        }

        assertArrayEquals(inBytes, outBytes);
    }
}
