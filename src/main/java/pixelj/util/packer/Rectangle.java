package pixelj.util.packer;

public final class Rectangle<M> {
    private final int id;
    private final int width;
    private final int height;
    private final M metadata;

    private int x;
    private int y;

    public Rectangle(final int id, final int width, final int height, final M metadata) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.metadata = metadata;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public M getMetadata() {
        return metadata;
    }

    /**
     * Move the rectangle.
     * 
     * @param destX Destination x coordinate
     * @param destY Destination y coordinate
     */
    public void moveTo(final int destX, final int destY) {
        this.x = destX;
        this.y = destY;
    }
}
