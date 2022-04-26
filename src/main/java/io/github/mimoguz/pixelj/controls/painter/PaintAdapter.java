package io.github.mimoguz.pixelj.controls.painter;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.eclipse.jdt.annotation.NonNull;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

/**
 * Adds mouse drawing support to a Painter.
 */
public class PaintAdapter implements MouseListener, MouseMotionListener {
    private int extent = 0;
    private boolean fill = false;
    private final Point lastPixel = new Point(-1, -1);
    @NonNull
    private final Painter painter;
    private boolean symmetrical = false;

    public PaintAdapter(@NonNull Painter painter) {
        this.painter = painter;
    }

    /**
     * The last column of the main drawing area. It's used to calculate the vertical
     * symmetry axis.
     */
    public int getExtent() {
        return extent;
    }

    /**
     * Is the vertical symmetry enabled?
     */
    public boolean isSymmetrical() {
        return symmetrical;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        final var model = painter.getModel();
        if (model == null || e == null || isOutside()) {
            return;
        }

        final var image = model.getGlyph();
        final var pt = getPixelCoordinates(image, e);
        if (pt.x != lastPixel.x || pt.y != lastPixel.y) {
            lastPixel.setLocation(pt);
            setPixel(image);
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        setOutside();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final var model = painter.getModel();
        if (model == null || e == null) {
            return;
        }

        painter.takeSnapshot();

        final var image = model.getGlyph();
        lastPixel.setLocation(getPixelCoordinates(image, e));
        // The button1 (or any other button while control pressed) clears, any other
        // button besides the button1 paints.
        fill = !(e.getButton() == MouseEvent.BUTTON1
                && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0);
        setPixel(image);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        setOutside();
    }

    /**
     * The last column of the main drawing area. It's used to calculate vertical
     * symmetry axis.
     */
    public void setExtent(final int value) {
        extent = value;
    }

    /**
     * Enable/disable the vertical symmetry.
     */
    public void setSymmetrical(final boolean value) {
        symmetrical = value;
    }

    private Point getPixelCoordinates(@NonNull final BinaryImage image, @NonNull final MouseEvent e) {
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

    private void setPixel(@NonNull BinaryImage image) {
        image.set(lastPixel.x, lastPixel.y, fill);
        if (symmetrical && lastPixel.x < extent) {
            final var mirrorX = extent - lastPixel.x - 1;
            image.set(mirrorX, lastPixel.y, fill);
        }
    }
}
