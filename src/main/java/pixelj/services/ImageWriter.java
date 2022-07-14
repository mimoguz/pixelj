package pixelj.services;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collection;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.DocumentSettings;
import pixelj.models.SortedList;
import pixelj.util.packer.Rectangle;

public interface ImageWriter {
    /**
     * Opaque ARGB.
     */
    int OPAQUE = 0xff_ff_ff_ff;
    /**
     * Transparent ARGB.
     */
    int TRANSPARENT = 0x00_ff_ff_ff;

    /**
     * Get color data of a pixel.
     *
     * @param source
     * @param px X coordinate of the pixel
     * @param py Y coordinate of the pixel
     * @return ARGB value
     */
    default int getARGB(BinaryImage source, int px, int py) {
        return source.get(px, py) ? TRANSPARENT : OPAQUE;
    }

    /**
     * Create an image and write glyph images to it.
     *
     * @param imageSize Result image size
     * @param rectangles Glyph placement
     * @param glyphs Glyph collection
     * @param settings Document settings
     * @return Created image. Its image type must be TYPE_INT_ARGB.
     */
    BufferedImage getImage(
            Dimension imageSize,
            Collection<Rectangle<GlyphImageData>> rectangles,
            SortedList<Glyph> glyphs,
            DocumentSettings settings
    );
}
