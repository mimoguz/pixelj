package io.github.pixelj.controls.painter;

import io.github.pixelj.graphics.BinaryImage;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Adds mouse drawing support to a Painter.
 */
public class PaintAdapter implements MouseListener, MouseMotionListener {
    private final Point lastPixel = new Point(-1, -1);
    private final Painter painter;
    private int extent = 0;
    private boolean fill = false;
    private boolean symmetrical = false;

    public PaintAdapter(Painter painter) {
        this.painter = painter;
    }

    /**
     * The last column of the main drawing area. It's used to calculate the vertical symmetry axis.
     */
    public int getExtent() {
        return extent;
    }

    /**
     * The last column of the main drawing area. It's used to calculate vertical symmetry axis.
     */
    public void setExtent(int value) {
        extent = value;
    }

    /**
     * Is the vertical symmetry enabled?
     */
    public boolean isSymmetrical() {
        return symmetrical;
    }

    /**
     * Enable/disable the vertical symmetry.
     */
    public void setSymmetrical(boolean value) {
        symmetrical = value;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        final var model = painter.getModel();
        if (model == null || isOutside()) return;

        final var image = model.getGlyph();
        final var pt = getPixelCoordinates(image, e);
        if (pt.x != lastPixel.x || pt.y != lastPixel.y) {
            lastPixel.setLocation(pt);
            setPixel(image);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setOutside();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        final var model = painter.getModel();
        if (model == null) return;

        final var image = model.getGlyph();
        painter.addSnapshot(image.getSnapshot(model.getCodePoint()));

        lastPixel.setLocation(getPixelCoordinates(image, e));
        // The button1 -or any other button while control pressed- clears, any other button besides the button1 paints.
        fill = !(e.getButton() == MouseEvent.BUTTON1 && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0);
        setPixel(image);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setOutside();
    }

    private Point getPixelCoordinates(BinaryImage image, MouseEvent e) {
        final var cellWidth = painter.getWidth() / image.getWidth();
        final var cellHeight = painter.getHeight() / image.getHeight();
        final var x = e.getX() / cellWidth;
        final var y = e.getY() / cellHeight;
        return new Point(x, y);
    }

    private boolean isOutside() {
        return lastPixel.x < 0 || lastPixel.y < 0;
    }

    private void setOutside() {
        lastPixel.setLocation(-1, -1);
    }

    private void setPixel(BinaryImage image) {
        image.set(lastPixel.x, lastPixel.y, fill);
        if (symmetrical && lastPixel.x < extent) {
            final var mirrorX = extent - lastPixel.x - 1;
            image.set(mirrorX, lastPixel.y, fill);
        }
    }
}
