package pixelj.util.packer;

import java.util.List;
import java.awt.Dimension;

public abstract class Packer {
    protected List<Rectangle> rectangles;

    public Packer(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    /**
     * Calculate box size and move inputs
     * 
     * @return Box size
     */
    public abstract Dimension packInPlace(boolean forceSquare, boolean forcePowerOfTwo);

    protected static Dimension fixedSize(
            final Dimension size,
            final boolean forceSquare,
            final boolean forcePowerOfTwo
    ) {
        final var result = new Dimension(size);
        if (forceSquare) {
            final var sz = Math.max(result.width, result.height);
            result.setSize(sz, sz);
        }
        if (forcePowerOfTwo) {
            result.setSize(smallestPowerOfTwo(result.width), smallestPowerOfTwo(result.height));
        }
        return result;
    }

    protected static int smallestPowerOfTwo(int n) {
        assert (n > 0);
        final var pow = (int) Math.ceil(Math.log(n) / Math.log(2.0));
        return 2 << (pow - 1);
    }
}
