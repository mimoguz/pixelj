package pixelj.services;

import java.awt.Dimension;
import java.util.Collection;
import java.awt.image.BufferedImage;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.SortedList;
import pixelj.util.packer.Rectangle;

public class SingleThreadedImageWriter implements ImageWriter {
    @Override
    public BufferedImage getImage(
            final Dimension imageSize,
            final Collection<Rectangle> rectangles,
            final SortedList<Glyph> glyphs
    ) {
        final var image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        for (var rect : rectangles) {
            final var glyph = glyphs.findHash(rect.id());
            // TODO: Better way to carry actual glyph size
            writeRect(image, rect.x(), rect.y(), rect.width() - 1, rect.height() - 1, glyph.getImage());
        }
        return image;
    }

    private void writeRect(
            BufferedImage target,
            final int x,
            final int y,
            final int width,
            final int height,
            final BinaryImage source
    ) {
        for (var py = 0; py < height; py++) {
            for (var px = 0; px < width; px++) {
                target.setRGB(x + px, y + py, getARGB(source, px, py));
            }
        }
    }
}
