package pixelj.services;

import java.awt.Dimension;
import java.util.Collection;
import java.awt.image.BufferedImage;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.Metrics;
import pixelj.models.SortedList;
import pixelj.util.packer.Rectangle;

public class BasicImageWriter implements ImageWriter {
    @Override
    public BufferedImage getImage(
            final Dimension imageSize,
            final Collection<Rectangle> rectangles,
            final SortedList<Glyph> glyphs,
            final Metrics metrics
    ) {
        final var image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        final var sourceY = metrics.canvasHeight() - metrics.ascender() - metrics.descender();
        final var sourceHeight = metrics.ascender() + metrics.descender();
        for (var rect : rectangles) {
            final var glyph = glyphs.findHash(rect.id());
            final var sourceWidth = metrics.isMonospaced() ? metrics.defaultWidth() : glyph.getWidth();
            writeRect(image, rect.x(), rect.y(), glyph.getImage(), 0, sourceY, sourceWidth, sourceHeight);
        }
        return image;
    }

    private void writeRect(
            BufferedImage target,
            final int targetX,
            final int targetY,
            final BinaryImage source,
            final int sourceX,
            final int sourceY,
            final int sourceWidth,
            final int sourceHeight
    ) {
        for (var py = 0; py < sourceHeight; py++) {
            for (var px = 0; px < sourceWidth; px++) {
                target.setRGB(targetX + px, targetY + py, getARGB(source, px + sourceX, py + sourceY));
            }
        }
    }
}
