package io.github.mimoguz.pixelj.graphics;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class AndComposite implements Composite {
    public static class IntCompositeContext implements CompositeContext {
        @Override
        public void compose(final Raster source, final Raster in, final WritableRaster out) {
            final var height = Math.min(in.getHeight(), out.getHeight());
            final var width = Math.min(in.getWidth(), out.getWidth());

            final var sourceBuffer = new int[width];
            final var inBuffer = new int[width];
            final var outBuffer = new int[width];

            for (var y = 0; y < height; y++) {
                in.getDataElements(0, y, width, 1, inBuffer);
                source.getDataElements(0, y, width, 1, sourceBuffer);
                for (var x = 0; x < width; x++) {
                    outBuffer[x] = inBuffer[x] & sourceBuffer[x];
                }
                out.setDataElements(0, y, width, 1, outBuffer);
            }
        }

        @Override
        public void dispose() {
            // Nothing to dispose
        }
    }

    private static class ByteCompositeContext implements CompositeContext {
        @Override
        public void compose(final Raster source, final Raster in, final WritableRaster out) {
            final var height = Math.min(in.getHeight(), out.getHeight());
            final var width = Math.min(in.getWidth(), out.getWidth());

            final var sourceBuffer = new byte[width];
            final var inBuffer = new int[width];
            final var outBuffer = new int[width];

            for (var y = 0; y < height; y++) {
                in.getDataElements(0, y, width, 1, inBuffer);
                source.getDataElements(0, y, width, 1, sourceBuffer);
                for (var x = 0; x < width; x++) {
                    outBuffer[x] = inBuffer[x] & (sourceBuffer[x] == 1 ? 0xff_ff_ff_ff : 0);
                }
                out.setDataElements(0, y, width, 1, outBuffer);
            }
        }

        @Override
        public void dispose() {
            // Nothing to dispose
        }
    }

    private static final int RGB_PIXEL_SIZE = 24;

    @Override
    public CompositeContext createContext(
            final ColorModel sourceColorModel,
            final ColorModel destinationColorModel,
            final RenderingHints hints
    ) {
        if (destinationColorModel.getPixelSize() < RGB_PIXEL_SIZE) {
            throw new IllegalArgumentException(
                    "This destination color model isn't supported by the AndComposite"
            );
        }
        if (sourceColorModel.getPixelSize() <= 8) {
            return new ByteCompositeContext();
        } else {
            return new IntCompositeContext();
        }
    }
}
