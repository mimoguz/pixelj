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
}
