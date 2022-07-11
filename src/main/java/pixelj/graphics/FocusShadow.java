package pixelj.graphics;


import javax.imageio.ImageIO;
import javax.swing.border.Border;

import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

public final class FocusShadow implements Border {

    private static final int SIZE = 8;
    private static final int TILE_SIZE = 16;
    private static final int TOP_LEFT = 0;
    private static final int TOP = 1;
    private static final int TOP_RIGHT = 2;
    private static final int RIGHT = 3;
    private static final int BOTTOM_RIGHT = 4;
    private static final int BOTTOM = 5;
    private static final int BOTTOM_LEFT = 6;
    private static final int LEFT = 7;
    private static final Insets BORDER_INSETS = new Insets(SIZE, SIZE, SIZE, SIZE);

    private final IntObjectHashMap<Image> images;

    public FocusShadow() throws IOException {
        this.images = new IntObjectHashMap<Image>(8);
        images.put(TOP_LEFT, ImageIO.read(getClass().getResourceAsStream("shadow/top_left.png")));
        images.put(TOP, ImageIO.read(getClass().getResourceAsStream("shadow/top.png")));
        images.put(TOP_RIGHT, ImageIO.read(getClass().getResourceAsStream("shadow/top_right.png")));
        images.put(RIGHT, ImageIO.read(getClass().getResourceAsStream("shadow/right.png")));
        images.put(BOTTOM_RIGHT, ImageIO.read(getClass().getResourceAsStream("shadow/bottom_right.png")));
        images.put(BOTTOM, ImageIO.read(getClass().getResourceAsStream("shadow/bottom.png")));
        images.put(BOTTOM_LEFT, ImageIO.read(getClass().getResourceAsStream("shadow/bottom_left.png")));
        images.put(LEFT, ImageIO.read(getClass().getResourceAsStream("shadow/left.png")));
    }

    @Override
    public void paintBorder(
            final Component c,
            final Graphics g,
            final int x,
            final int y,
            final int width,
            final int height
    ) {
        final var g2d = (Graphics2D) g.create();

        // Draw top and bottom
        final var hDim = width;
        final var bottomY = height - SIZE;
        var px = x;
        while (px < hDim) {
            g2d.drawImage(images.get(TOP), px, y, null);
            g2d.drawImage(images.get(BOTTOM), px, bottomY, null);
            px += TILE_SIZE;
        }

        // Draw left and right
        final var vDim = height;
        final var rightX = width - SIZE;
        var py = y;
        while (py < vDim) {
            g2d.drawImage(images.get(LEFT), x, py, null);
            g2d.drawImage(images.get(RIGHT), rightX, py, null);
            py += TILE_SIZE;
        }

        // Draw corners
        g2d.setColor(c.getBackground());
        g2d.fillRect(x, y, SIZE, SIZE);
        g2d.drawImage(images.get(TOP_LEFT), x, y, null);
        g2d.fillRect(x + width - SIZE, y, SIZE, SIZE);
        g2d.drawImage(images.get(TOP_RIGHT), x + height - SIZE, y, null);
        g2d.fillRect(x + width - SIZE, y + height - SIZE, SIZE, SIZE);
        g2d.drawImage(images.get(BOTTOM_RIGHT), x + height - SIZE, width + y - SIZE, null);
        g2d.fillRect(x, y + height - SIZE, SIZE, SIZE);
        g2d.drawImage(images.get(BOTTOM_LEFT), x, width + y - SIZE, null);

        g2d.dispose();
    }

    public int getSize() {
        return SIZE;
    }

    @Override
    public Insets getBorderInsets(final Component c) {
        return BORDER_INSETS;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}