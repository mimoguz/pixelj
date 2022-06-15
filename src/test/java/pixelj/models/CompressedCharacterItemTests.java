package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import pixelj.graphics.BinaryImage;
import pixelj.models.CompressedCharacterItem.MisshapenDataException;

public class CompressedCharacterItemTests {
    @Test
    public void testSquare() {
        final var size = 16;
        final var capitalA = new CharacterItem(65, 10, BinaryImage.of(size, size, false));
        for (var i = 0; i < size; i++) {
            capitalA.getGlyph().set(i, i, true);
        }

        final var compressed = CompressedCharacterItem.from(capitalA);

        final var inBytes = new byte[size * size];
        final var outBytes = new byte[size * size];

        capitalA.getGlyph().getDataElements(0, 0, size, size, inBytes);
        try {
            final var decompressed = compressed.getCharacterItem(size, size);
            decompressed.getGlyph().getDataElements(0, 0, size, size, outBytes);
        } catch (MisshapenDataException exception) {
            throw new RuntimeException("Couldn't decompress back?");
        }

        assertArrayEquals(inBytes, outBytes);
    }

    public void testRectangular() {
        final var width = 8;
        final var height = 21;

        final var capitalA = new CharacterItem(65, 10, BinaryImage.of(width, height, false));
        final var min = Math.min(width, height);
        for (var i = 0; i < min; i++) {
            capitalA.getGlyph().set(i, i, true);
        }

        final var compressed = CompressedCharacterItem.from(capitalA);

        final var inBytes = new byte[width * height];
        final var outBytes = new byte[width * height];

        capitalA.getGlyph().getDataElements(0, 0, width, height, inBytes);
        try {
            final var decompressed = compressed.getCharacterItem(width, height);
            decompressed.getGlyph().getDataElements(0, 0, width, height, outBytes);
        } catch (MisshapenDataException exception) {
            throw new RuntimeException("Couldn't decompress back?");
        }

        assertArrayEquals(inBytes, outBytes);
    }
}
