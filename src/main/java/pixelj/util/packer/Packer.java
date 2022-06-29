package pixelj.util.packer;

import java.awt.Dimension;
import java.util.List;

public interface Packer {
    void setRectangles(List<Rectangle> rectangles);

    List<Rectangle> getRectangles();

    /**
     * Calculate box size and move inputs
     * 
     * @return Box size
     */
    Dimension packInPlace(boolean forceSquare, boolean forcePowerOfTwo) throws IllegalStateException;
}
