package pixelj.services;

import pixelj.graphics.BinaryImage;
import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.util.packer.Rectangle;

public record GlyphImageData(
        int glyphWidth,
        int glyphHeight,
        int clipWidth,
        int clipHeight,
        int xOffset,
        int yOffset
) {

    /**
     * Find the rectangle that snugly fits the non-empty pixels of the source glyph.
     *
     * @param glyph
     * @param settings
     * @return Snug-fitting rectangle
     */
    public static Rectangle<GlyphImageData> findFitting(final Glyph glyph, final DocumentSettings settings) {
        final var glyphHeight = settings.ascender() + settings.descender();
        final var glyphWidth = settings.isMonospaced()
                    ? Math.min(glyph.getWidth(), settings.defaultWidth())
                    : glyph.getWidth();

        final var colBuffer = new byte[glyphHeight];
        final var rowBuffer = new byte[glyphWidth];
        final var img = glyph.getImage();
        final var topPad = settings.canvasHeight() - glyphHeight;

        // Find first x
        var left = 0;
        for (; left < glyphWidth; left++) {
            img.getDataElements(left, 0, 1, glyphHeight, colBuffer);
            if (isNotEmpty(colBuffer)) {
                break;
            }
        }

        // Find last x
        var right = glyphWidth - 1;
        for (; right >= 0; right--) {
            img.getDataElements(right, 0, 1, glyphHeight, colBuffer);
            if (isNotEmpty(colBuffer)) {
                break;
            }
        }

        // Find first y
        var top = 0;
        for (; top < glyphHeight; top++) {
            img.getDataElements(0, top + topPad, glyphWidth, 1, rowBuffer);
            if (isNotEmpty(rowBuffer)) {
                break;
            }
        }

        // Find last y
        var bottom = glyphHeight - 1;
        for (; bottom >= 0; bottom--) {
            img.getDataElements(0, bottom + topPad, glyphWidth, 1, rowBuffer);
            if (isNotEmpty(rowBuffer)) {
                break;
            }
        }

        final var clipWidth = right - left + 1;
        final var clipHeight = bottom - top + 1;

        final var md = new GlyphImageData(glyphWidth, glyphHeight, clipWidth, clipHeight, left, top);
        return new Rectangle<>(glyph.getCodePoint(), clipWidth + 1, clipHeight + 1, md);
    }

    /**
     * Find the rectangle that encloses glyph width - glyph height.
     *
     * @param glyph
     * @param settings
     * @return Loose-fitting rectangle
     */
    public static Rectangle<GlyphImageData> findLoose(final Glyph glyph, final DocumentSettings settings) {
        final var glyphHeight = settings.ascender() + settings.descender();
        final var glyphWidth = settings.isMonospaced()
                    ? Math.min(glyph.getWidth(), settings.defaultWidth())
                    : glyph.getWidth();
        final var md = new GlyphImageData(glyphWidth, glyphHeight, glyphWidth, glyphHeight, 0, 0);
        return new Rectangle<>(glyph.getCodePoint(), glyphWidth + 1, glyphHeight + 1, md);
    }

    /**
     * @param image Source
     * @return Data elements defined by this.
     */
    public byte[] extract(final BinaryImage image) {
        final var buffer = new byte[clipWidth * clipHeight];
        final var topPad = image.getImageHeight() - glyphHeight;
        return image.getDataElements(xOffset, yOffset + topPad, clipWidth, clipHeight, buffer);
    }

    private static boolean isNotEmpty(final byte[] line) {
        for (var i = 0; i < line.length; i++) {
            if (line[i] == 0) {
                return true;
            }
        }
        return false;
    }
}
