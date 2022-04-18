package io.github.mimoguz.pixelj.graphics;

import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;

public class BinaryImage
        extends Image
        implements Changeable<BinaryImage, BinaryImage.ImageChangeEvent, BinaryImage.ImageChangeListener> {
    private static final byte b0 = 0;
    private static final byte b1 = 1;
    protected final BufferedImage image;
    private final EventListenerList listeners = new EventListenerList();
    private final byte[] pixelBuffer = new byte[1];
    private final WritableRaster raster;

    private BinaryImage(final int width, final int height, final @NotNull BufferedImage image) {
        this.image = image;
        raster = image.getRaster();
    }

    public static BinaryImage from(final @NotNull BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_BYTE_BINARY) {
            throw new IllegalArgumentException("Image is not binary indexed");
        }

        if (image.getColorModel() instanceof IndexColorModel cm && cm.getMapSize() != 2) {
            throw new IllegalArgumentException("Image is not binary indexed");
        }

        return new BinaryImage(image.getWidth(), image.getHeight(), image);
    }

    public static BinaryImage of(final int width, final int height, final boolean fill) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        var binaryImage = new BinaryImage(width, height, img);
        binaryImage.fill(fill, false);
        return binaryImage;
    }

    public static BinaryImage of(final int width, final int height) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        return new BinaryImage(width, height, img);
    }

    public void draw(final @NotNull Graphics2D graphics, final int x, final int y, final int width, final int height) {
        graphics.drawImage(image, x, y, width, height, null);
    }

    public void fill(final boolean value, final boolean notify) {
        var lineBuffer = new byte[image.getWidth()];
        Arrays.fill(lineBuffer, (value ? b1 : b0));
        for (var y = 0; y < image.getHeight(); y++) {
            raster.setDataElements(0, y, image.getWidth(), 1, lineBuffer);
        }
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void fill(final boolean value) {
        fill(value, true);
    }

    @Override
    public void flush() {
        image.flush();
        super.flush();
    }

    public boolean get(final int x, final int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0] == b1;
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
    }

    public ColorModel getColorModel() {
        return image.getColorModel();
    }

    public byte[] getDataElements(final int x, final int y, final int width, final int height, final byte[] target) {
        raster.getDataElements(x, y, width, height, target);
        return target;
    }

    @Override
    public Graphics getGraphics() {
        return image.getGraphics();
    }

    public int getHeight() {
        return getHeight(null);
    }

    @Override
    public int getHeight(final @Nullable ImageObserver observer) {
        return image.getHeight(observer);
    }

    @Override
    public @NotNull Class<ImageChangeListener> getListenerClass() {
        return ImageChangeListener.class;
    }

    @Override
    public @NotNull EventListenerList getListenerList() {
        return listeners;
    }

    @Override
    public Object getProperty(final String name, final @Nullable ImageObserver observer) {
        return image.getProperty(name, observer);
    }

    public Raster getRaster() {
        return image.getData();
    }

    @Override
    public Image getScaledInstance(final int width, final int height, final int hints) {
        return image.getScaledInstance(width, height, hints);
    }

    public Snapshot getSnapshot(final int id) {
        var buffer = new byte[getWidth() * getHeight()];
        raster.getDataElements(0, 0, getWidth(), getHeight(), buffer);
        return new Snapshot(id, getWidth(), getHeight(), buffer);
    }

    @Override
    public ImageProducer getSource() {
        return image.getSource();
    }

    public int getWidth() {
        return getWidth(null);
    }

    @Override
    public int getWidth(final @Nullable ImageObserver observer) {
        return image.getWidth(observer);
    }

    public void invert(final int x, final int y, final boolean notify) {
        set(x, y, !get(x, y));
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void invert(final int x, final int y) {
        invert(x, y, true);
    }

    public void requestUpdate() {
        fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
    }

    public void set(final int x, final int y, final boolean value, final boolean notify) {
        pixelBuffer[0] = value ? b1 : b0;
        raster.setDataElements(x, y, pixelBuffer);
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void set(final int x, final int y, final boolean value) {
        set(x, y, value, true);
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
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void setDataElements(final int x, final int y, final int width, final int height, final byte[] source) {
        setDataElements(x, y, width, height, source, true);
    }

    private void setByteValue(final int x, final int y, final byte value) {
        pixelBuffer[0] = value;
        raster.setDataElements(x, y, pixelBuffer);
    }

    public enum ImageChangeEvent {
        IMAGE_MODIFIED
    }

    public interface ImageChangeListener extends ChangeListener<BinaryImage, ImageChangeEvent> {
    }
}
