package pixelj.services;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collection;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.Metrics;
import pixelj.models.SortedList;
import pixelj.util.packer.Rectangle;

public interface ImageWriter {
    int OPAQUE = 0xff_ff_ff_ff;
    int TRANSPARENT = 0x00_ff_ff_ff;

    default int getARGB(BinaryImage source, int px, int py) {
        return source.get(px, py) ? TRANSPARENT : OPAQUE;
    }

    BufferedImage getImage(
            final Dimension imageSize,
            final Collection<Rectangle> rectangles,
            final SortedList<Glyph> glyphs,
            final Metrics metrics
    );
}
