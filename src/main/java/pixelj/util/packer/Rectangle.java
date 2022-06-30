package pixelj.util.packer;

public class Rectangle {
    private final int id;
    private final int width;
    private final int height;
    private final int innerWidth;
    private final int innerHeight;

    private int x = 0;
    private int y = 0;

    public Rectangle(int id, int width, int height, int innerWidth, int innerHeight) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.innerWidth = innerWidth;
        this.innerHeight = innerHeight;
    }

    public int id() {
        return id;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int innerWidth() {
        return innerWidth;
    }

    public int innerHeight() {
        return innerHeight;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
