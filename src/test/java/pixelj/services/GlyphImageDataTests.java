package pixelj.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pixelj.graphics.BinaryImage;
import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;

public class GlyphImageDataTests {
    private static final byte BLACK = 0;
    private static final byte WHITE = 1;
    private static final int LOWERCASE_A = 61;
    private static final int A_WIDTH = 4;
    private static final int A_HEIGHT = 4;
    private static final DocumentSettings SETTINGS = DocumentSettings .getDefault();
    private static final byte[] A_BYTES = new byte[] {
            WHITE, BLACK, BLACK, BLACK,
            BLACK, WHITE, WHITE, BLACK,
            BLACK, WHITE, WHITE, BLACK,
            WHITE, BLACK, BLACK, BLACK
    };

    /** Test loose fit. */
    @Test
    public void looseFit() {
        final var a = makeLowercaseA(0, 0);
        final var rect = GlyphImageData.findLoose(a, SETTINGS);
        final var md = rect.getMetadata();

        assertEquals(a.getWidth() + 1, rect.getWidth());
        assertEquals(SETTINGS.ascender() + SETTINGS.descender() + 1, rect.getHeight());
        assertEquals(0, md.xOffset());
        assertEquals(0, md.yOffset());
        assertEquals(a.getWidth(), md.clipWidth());
        assertEquals(SETTINGS.ascender() + SETTINGS.descender(), md.clipHeight());
    }

    /** Test snug fit. */
    @Test
    public void snugFit() {
        final var a = makeLowercaseA(0, 0);
        final var rect = GlyphImageData.findFitting(a, SETTINGS);
        final var md = rect.getMetadata();

        assertEquals(A_WIDTH + 1, rect.getWidth());
        assertEquals(A_HEIGHT + 1, rect.getHeight());
        assertEquals(0, md.xOffset());
        assertEquals(SETTINGS.ascender() - A_HEIGHT, md.yOffset());
        assertEquals(A_WIDTH, md.clipWidth());
        assertEquals(A_HEIGHT, md.clipHeight());
    }

    /** Test snug fit with empty columns on each side. */
    @Test
    public void snugFitEmptyCols() {
        final var a = makeLowercaseA(1, 0);
        a.setWidth(A_WIDTH + 2);
        final var rect = GlyphImageData.findFitting(a, SETTINGS);
        final var md = rect.getMetadata();

        assertEquals(A_WIDTH + 1, rect.getWidth());
        assertEquals(A_HEIGHT + 1, rect.getHeight());
        assertEquals(1, md.xOffset());
        assertEquals(SETTINGS.ascender() - A_HEIGHT, md.yOffset());
        assertEquals(A_WIDTH, md.clipWidth());
        assertEquals(A_HEIGHT, md.clipHeight());
        assertEquals(a.getWidth(), md.glyphWidth());
    }

    private static Glyph makeLowercaseA(final int x, final int y) {
        final var a = new Glyph(
                LOWERCASE_A,
                4,
                BinaryImage.of(SETTINGS.canvasWidth(), SETTINGS.canvasHeight())
        );
        a.getImage().fill(true);
        a.getImage().setDataElements(
                x,
                y + SETTINGS.canvasHeight() - SETTINGS.descender() - A_HEIGHT,
                A_WIDTH,
                A_HEIGHT,
                A_BYTES
        );
        return a;
    }
}
