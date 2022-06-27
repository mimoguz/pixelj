package pixelj.util.packer;

public class Rectangle {
    private final int id;
    private final int width;
    private final int height;
    private int x = 0;
    private int y = 0;

    public Rectangle(int id, int width, int height) {
        this.id = id;
        this.height = height;
        this.width = width;
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

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
