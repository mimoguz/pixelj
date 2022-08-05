package pixelj.util;

import static pixelj.util.MathUtils.even;
import static pixelj.util.MathUtils.odd;

import java.awt.image.BufferedImage;

public final class Checkerboard {

    private static final int BLACK = 0x10_00_00_00;
    private static final int WHITE = 0x30_ff_ff_ff;

    private Checkerboard() {
    }

    /**
     * @param width Image width
     * @param height Image height
     * @return An image with checkerboard pattern
     */
    public static BufferedImage create(final int width, final int height) {
        final var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                image.setRGB(x, y, ((odd(x) && even(y)) || (odd(y) && even(x))) ? WHITE : BLACK);
            }
        }
        return image;
    }

}
