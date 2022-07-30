package pixelj.views.controls.painter;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pixelj.graphics.BinaryImage;

/** Adds mouse drawing support to a Painter. */
public final class PaintAdapter implements MouseListener, MouseMotionListener {

    private int extent;
    private boolean fill;
    private final Point lastPixel = new Point(-1, -1);

    private final Painter painter;
    private boolean symmetrical;

    public PaintAdapter(final Painter painter) {
        this.painter = painter;
    }

    /**
     * The last column of the main drawing area. It's used to calculate the vertical
     * symmetry axis.
     *
     * @return Extent.
     */
    public int getExtent() {
        return extent;
    }

    /**
     * The last column of the main drawing area. It's used to calculate vertical
     * symmetry axis.
     *
     * @param value Extent value
     */
    public void setExtent(final int value) {
        extent = value;
    }

    /**
     * Is the vertical symmetry enabled?
     *
     * @return True if enabled, false otherwise.
     */
    public boolean isSymmetrical() {
        return symmetrical;
    }

    /**
     * Enable/disable the vertical symmetry.
     *
     * @param value Is symmetrical.
     */
    public void setSymmetrical(final boolean value) {
        symmetrical = value;
    }

    @Override
    public void mouseClicked(final MouseEvent e) { // Ignore
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        final var model = painter.getModel();
        if (model == null || e == null || isOutside()) {
            return;
        }

        final var image = model.getImage();
        final var pt = getPixelCoordinates(image, e);
        if (pt.x != lastPixel.x || pt.y != lastPixel.y) {
            lastPixel.setLocation(pt);
            setPixel(image);
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) { // Ignore
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        setOutside();
    }

    @Override
    public void mouseMoved(final MouseEvent e) { // Ignore
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        final var model = painter.getModel();
        if (model == null || e == null) {
            return;
        }

        painter.takeSnapshot();

        final var image = model.getImage();
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

    private Point getPixelCoordinates(final BinaryImage image, final MouseEvent e) {
        final var cellWidth = painter.getWidth() / image.getImageWidth();
        final var cellHeight = painter.getHeight() / image.getImageHeight();
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

    private void setPixel(final BinaryImage image) {
        image.set(lastPixel.x, lastPixel.y, fill);
        if (symmetrical && lastPixel.x < extent) {
            final var mirrorX = extent - lastPixel.x - 1;
            image.set(mirrorX, lastPixel.y, fill);
        }
    }
}
