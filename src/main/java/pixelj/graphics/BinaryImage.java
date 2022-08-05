package pixelj.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import pixelj.util.ChangeListener;
import pixelj.util.Changeable;

public final class BinaryImage
    extends Image
    implements Changeable<BinaryImage, Void, BinaryImage.ImageListener> {

    private static final byte BYTE_0 = 0;
    private static final byte BYTE_1 = 1;
    private static final String NOT_BINARY_INDEXED = "Image is not binary indexed";

    private final Set<ImageListener> listeners = new HashSet<>();
    private final byte[] pixelBuffer = new byte[1];
    private final WritableRaster raster;
    private final BufferedImage image;

    private BinaryImage(final int width, final int height, final BufferedImage image) {
        if (image.getWidth() != width || image.getHeight() != height) {
            throw new IllegalArgumentException("Dimensions don't match");
        }
        this.image = image;
        raster = image.getRaster();
    }

    /**
     * Graphics.drawImage won't work. Use this to draw.
     *
     * @param graphics
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void draw(final Graphics2D graphics, final int x, final int y, final int width, final int height) {
        graphics.drawImage(image, x, y, width, height, null);
    }

    /**
     * Fill all pixels with the value. Notify after.
     *
     * @param value Fill value
     */
    public void fill(final boolean value) {
        fill(value, true);
    }

    /**
     * Fill all pixels with the value.
     *
     * @param value  Fill value
     * @param notify true -> notify after | false -> don't notify
     */
    public void fill(final boolean value, final boolean notify) {
        final var lineBuffer = new byte[image.getWidth()];
        Arrays.fill(lineBuffer, (value ? BYTE_1 : BYTE_0));
        for (var y = 0; y < image.getHeight(); y++) {
            raster.setDataElements(0, y, image.getWidth(), 1, lineBuffer);
        }
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    /**
     * @param x X coordinate
     * @param y Y coordinate
     * @return Pixel value at (x, y)
     */
    public boolean get(final int x, final int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0] == BYTE_1;
    }

    /**
     * Set the pixel at (x, y) with the fill value, notify after.
     *
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param value Fill value
     */
    public void set(final int x, final int y, final boolean value) {
        set(x, y, value, true);
    }

    /**
     * Set the pixel at (x, y) with the fill value.
     *
     * @param x      X coordinate
     * @param y      Y coordinate
     * @param value  Fill value
     * @param notify true -> notify after | false -> don't notify
     */
    public void set(final int x, final int y, final boolean value, final boolean notify) {
        pixelBuffer[0] = value ? BYTE_1 : BYTE_0;
        raster.setDataElements(x, y, pixelBuffer);
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    /**
     * @param x X coordinate
     * @param y Y coordinate
     * @return Pixel value at (x, y)
     */
    public byte getByteValue(final int x, final int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0];
    }

    /**
     * @param x X coordinate
     * @param y Y coordinate
     * @param value Fill value
     */
    @SuppressWarnings("unused")
    private void setByteValue(final int x, final int y, final byte value) {
        pixelBuffer[0] = value;
        raster.setDataElements(x, y, pixelBuffer);
    }

    /**
     * @see java.awt.image.BufferedImage#getColorModel()
     *
     * @return The color model of the source image
     */
    public ColorModel getColorModel() {
        return image.getColorModel();
    }

    /**
     * @see java.awt.image.Raster#getDataElements(int, int, int, int, Object)
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param target Byte array of minimum size (width * height)
     * @return target
     */
    @SuppressWarnings("UnusedReturnValue")
    public byte[] getDataElements(
        final int x,
        final int y,
        final int width,
        final int height,
        final byte[] target
    ) {
        raster.getDataElements(x, y, width, height, target);
        return target;
    }

    /**
     * Set data elements of the rectangle given by (x, y) (width, height), notify after.
     * @see java.awt.image.WritableRaster#setDataElements(int, int, int, int, Object)
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param source Source data of minimum size (width * height)
     */
    public void setDataElements(
        final int x,
        final int y,
        final int width,
        final int height,
        final byte[] source
    ) {
        setDataElements(x, y, width, height, source, true);
    }

    /**
     * Set data elements of the rectangle given by (x, y) (width, height).
     * @see java.awt.image.WritableRaster#setDataElements(int, int, int, int, Object)
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param source Source data of minimum size (width * height)
     * @param notify true -> notify after | false -> don't notify
     */
    public void setDataElements(
        final int x,
        final int y,
        final int width,
        final int height,
        final byte[] source,
        final boolean notify
    ) {
        raster.setDataElements(x, y, width, height, source);
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    /**
     * Invert pixel value at (x, y).
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void invert(final int x, final int y) {
        invert(x, y, true);
    }

    /**
     * Invert pixel value at (x, y).
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param notify true -> notify after | false -> don't notify.
     */
    public void invert(final int x, final int y, final boolean notify) {
        set(x, y, !get(x, y));
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    /**
     * Force notify.
     */
    public void requestUpdate() {
        fireChangeEvent(this, null);
    }

    /**
     * @see java.awt.image.BufferedImage#getData()
     *
     * @return Source image raster
     */
    public Raster getRaster() {
        return image.getData();
    }

    /**
     * @param id Snapshot id
     * @return A snapshot of data elements
     */
    public Snapshot getSnapshot(final int id) {
        final var buffer = new byte[getImageWidth() * getImageHeight()];
        raster.getDataElements(0, 0, getImageWidth(), getImageHeight(), buffer);
        return new Snapshot(id, 0, 0, getImageWidth(), getImageHeight(), buffer);
    }

    /**
     * @see java.awt.image.BufferedImage#getSubimage(int, int, int, int)
     *
     * @param x       X coordinate
     * @param y       Y coordinate
     * @param width   Sub-image width
     * @param height  Sum-image height
     * @return Sub-image
     */
    public Image getSubImage(final int x, final int y, final int width, final int height) {
        return image.getSubimage(x, y, width, height);
    }

    public int getImageHeight() {
        return getHeight(null);
    }

    public int getImageWidth() {
        return getWidth(null);
    }

    @Override
    public void flush() {
        image.flush();
        super.flush();
    }

    @Override
    public float getAccelerationPriority() {
        return image.getAccelerationPriority();
    }

    @Override
    public ImageCapabilities getCapabilities(final GraphicsConfiguration gc) {
        return image.getCapabilities(gc);
    }

    @Override
    public int getHeight(final ImageObserver observer) {
        return image.getHeight(observer);
    }

    @Override
    public int getWidth(final ImageObserver observer) {
        return image.getWidth(observer);
    }

    @Override
    public Graphics getGraphics() {
        return image.getGraphics();
    }

    @Override
    public Set<ImageListener> getListeners() {
        return listeners;
    }

    @Override
    public Object getProperty(final String name, final ImageObserver observer) {
        return image.getProperty(name, observer);
    }

    @Override
    public Image getScaledInstance(final int width, final int height, final int hints) {
        return image.getScaledInstance(width, height, hints);
    }

    @Override
    public ImageProducer getSource() {
        return image.getSource();
    }

    @Override
    public void setAccelerationPriority(final float priority) {
        image.setAccelerationPriority(priority);
    }

    /**
     * @param image Source
     * @return A binary image that wraps the source
     */
    public static BinaryImage from(final BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_BYTE_BINARY) {
            throw new IllegalArgumentException(NOT_BINARY_INDEXED);
        }

        if (image.getColorModel() instanceof final IndexColorModel cm && cm.getMapSize() != 2) {
            throw new IllegalArgumentException(NOT_BINARY_INDEXED);
        }

        return new BinaryImage(image.getWidth(), image.getHeight(), image);
    }

    /**
     * @param width  Image width, > 0
     * @param height Image height, > 0
     * @return A new binary image
     */
    public static BinaryImage of(final int width, final int height) {
        final var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        return new BinaryImage(width, height, img);
    }

    /**
     * @param width  Image width, > 0
     * @param height Image height, > 0
     * @param fill   Initial value for pixels
     * @return A new binary image
     */
    public static BinaryImage of(final int width, final int height, final boolean fill) {
        final var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        final var binaryImage = new BinaryImage(width, height, img);
        binaryImage.fill(fill, false);
        return binaryImage;
    }

    public interface ImageListener extends ChangeListener<BinaryImage, Void> {
        // Empty
    }
}
