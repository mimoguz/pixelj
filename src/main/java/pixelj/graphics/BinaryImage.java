package pixelj.graphics;

import pixelj.util.ChangeListener;
import pixelj.util.Changeable;

import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BinaryImage extends Image
        implements
        Changeable<BinaryImage, Void, BinaryImage.ImageListener> {
    private static final byte BYTE_0 = 0;
    private static final byte BYTE_1 = 1;
    protected final BufferedImage image;
    private final Set<ImageListener> listeners = new HashSet<>();
    private final byte[] pixelBuffer = new byte[1];
    private final WritableRaster raster;

    private BinaryImage(final int width, final int height, final BufferedImage image) {
        if (image.getWidth() != width || image.getHeight() != height) {
            throw new IllegalArgumentException("Dimensions don't match");
        }
        this.image = image;
        raster = image.getRaster();
    }

    public void draw(final Graphics2D graphics, final int x, final int y, final int width, final int height) {
        graphics.drawImage(image, x, y, width, height, null);
    }

    public void fill(final boolean value) {
        fill(value, true);
    }

    public void fill(final boolean value, final boolean notify) {
        var lineBuffer = new byte[image.getWidth()];
        Arrays.fill(lineBuffer, (value ? BYTE_1 : BYTE_0));
        for (var y = 0; y < image.getHeight(); y++) {
            raster.setDataElements(0, y, image.getWidth(), 1, lineBuffer);
        }
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    @Override
    public void flush() {
        image.flush();
        super.flush();
    }// Idea does weird things with formatting

    public boolean get(final int x, final int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0] == BYTE_1;
    }

    @Override
    public float getAccelerationPriority() {
        return image.getAccelerationPriority();
    }

    @Override
    public void setAccelerationPriority(final float priority) {
        image.setAccelerationPriority(priority);
    }

    public byte getByteValue(final int x, final int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0];
    }

    @Override
    public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
        return image.getCapabilities(gc);
    } // Idea does weird things with formatting

    public ColorModel getColorModel() {
        return image.getColorModel();
    }

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

    @Override
    public Graphics getGraphics() {
        return image.getGraphics();
    }// Idea does weird things with formatting

    public int getHeight() {
        return getHeight(null);
    }

    @Override
    public int getHeight(final ImageObserver observer) {
        return image.getHeight(observer);
    }

    @Override
    public Set<ImageListener> getListeners() {
        return listeners;
    }

    @Override
    public Object getProperty(final String name, final ImageObserver observer) {
        return image.getProperty(name, observer);
    }

    public Raster getRaster() {
        return image.getData();
    }

    @Override
    public Image getScaledInstance(final int width, final int height, final int hints) {
        return image.getScaledInstance(width, height, hints);
    }// Idea does weird things with formatting

    public Snapshot getSnapshot(final int id) {
        var buffer = new byte[getWidth() * getHeight()];
        raster.getDataElements(0, 0, getWidth(), getHeight(), buffer);
        return new Snapshot(id, 0, 0, getWidth(), getHeight(), buffer);
    }

    @Override
    public ImageProducer getSource() {
        return image.getSource();
    }    // Idea does weird things with formatting

    public Image getSubImage(final int x, final int y, final int width, final int height) {
        return image.getSubimage(x, y, width, height);
    }

    public int getWidth() {
        return getWidth(null);
    }

    @Override
    public int getWidth(final ImageObserver observer) {
        return image.getWidth(observer);
    }

    public void invert(final int x, final int y) {
        invert(x, y, true);
    }

    public void invert(final int x, final int y, final boolean notify) {
        set(x, y, !get(x, y));
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    public void requestUpdate() {
        fireChangeEvent(this, null);
    }

    public void set(final int x, final int y, final boolean value) {
        set(x, y, value, true);
    }

    public void set(final int x, final int y, final boolean value, final boolean notify) {
        pixelBuffer[0] = value ? BYTE_1 : BYTE_0;
        raster.setDataElements(x, y, pixelBuffer);
        if (notify) {
            fireChangeEvent(this, null);
        }
    }

    public void setDataElements(
            final int x,
            final int y,
            final int width,
            final int height,
            final byte[] source
    ) {
        setDataElements(x, y, width, height, source, true);
    }

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

    @SuppressWarnings("unused")
    private void setByteValue(final int x, final int y, final byte value) {
        pixelBuffer[0] = value;
        raster.setDataElements(x, y, pixelBuffer);
    }

    public static BinaryImage from(final BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_BYTE_BINARY) {
            throw new IllegalArgumentException("Image is not binary indexed");
        }

        if (image.getColorModel() instanceof IndexColorModel cm && cm.getMapSize() != 2) {
            throw new IllegalArgumentException("Image is not binary indexed");
        }

        return new BinaryImage(image.getWidth(), image.getHeight(), image);
    }

    public static BinaryImage of(final int width, final int height) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        return new BinaryImage(width, height, img);
    }

    public static BinaryImage of(final int width, final int height, final boolean fill) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        var binaryImage = new BinaryImage(width, height, img);
        binaryImage.fill(fill, false);
        return binaryImage;
    }

    public interface ImageListener extends ChangeListener<BinaryImage, Void> {
        // Empty
    }
}