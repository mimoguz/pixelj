package pixelj.util.packer;

import java.util.List;

public interface Packer<M> {
    /**
     * Calculate box size and move inputs.
     *
     * @param input Rectangles to be packed into boxes
     * @param boxWith Width of each box
     * @param boxHeight Height of each box
     * @return List of rectangles per box.
     */
    List<List<Rectangle<M>>> pack(List<Rectangle<M>> input, int boxWith, int boxHeight)
            throws IllegalStateException;
}
