package pixelj.services;

import java.awt.Dimension;
import java.util.Collection;
import java.awt.image.BufferedImage;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.DocumentSettings;
import pixelj.models.SortedList;
import pixelj.util.packer.Rectangle;

public final class BasicImageWriter implements ImageWriter {
    @Override
    public BufferedImage getImage(
        final Dimension imageSize,
        final Collection<Rectangle<GlyphImageData>> rectangles,
        final SortedList<Glyph> glyphs,
        final DocumentSettings settings
    ) {
        final var image = new BufferedImage(
            imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB
        );
        final var top = settings.canvasHeight() - settings.ascender() - settings.descender();
        for (var rect : rectangles) {
            // Skip space
            if (rect.getId() == 32) {
                continue;
            }
            final var glyph = glyphs.findId(rect.getId());
            final var md = rect.getMetadata();
            writeRect(
                image,
                rect.getX(),
                rect.getY(),
                glyph.getImage(),
                md.xOffset(),
                top + md.yOffset(),
                md.clipWidth(),
                md.clipHeight()
            );
        }
        return image;
    }

    private void writeRect(
        final BufferedImage destination,
        final int destX,
        final int destY,
        final BinaryImage source,
        final int sourceX,
        final int sourceY,
        final int sourceWidth,
        final int sourceHeight
    ) {
        for (var py = 0; py < sourceHeight; py++) {
            for (var px = 0; px < sourceWidth; px++) {
                try {
                    destination.setRGB(destX + px, destY + py, getARGB(source, px + sourceX, py + sourceY));
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(String.format(
                        "DX: %d, DY: %d\nSX: %d, SY: %d\nSW: %d, SH: %d",
                        destX,
                        destY,
                        sourceX,
                        sourceY,
                        sourceWidth,
                        sourceHeight
                    ));
                    e.printStackTrace();
                }
            }
        }
    }
}
