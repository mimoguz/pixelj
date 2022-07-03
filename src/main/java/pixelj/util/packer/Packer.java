package pixelj.util.packer;

import java.util.List;

public interface Packer {
    /**
     * Calculate box size and move inputs.
     *
     * @param input     Rectangles to be packed into boxes
     * @param boxWith   Width of each box
     * @param boxHeight Height of each box
     * @return List of rectangles per box.
     */
    List<List<Rectangle>> pack(List<Rectangle> input, int boxWith, int boxHeight)
            throws IllegalStateException;
}
