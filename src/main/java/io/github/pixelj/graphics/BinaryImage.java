package io.github.pixelj.graphics;

import io.github.pixelj.util.ChangeListener;
import io.github.pixelj.util.Changeable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;

public class BinaryImage extends Image implements Changeable<BinaryImage, BinaryImage.ImageChangeEvent, BinaryImage.ImageChangeListener> {
    private static final byte b0 = 0;
    private static final byte b1 = 1;
    protected final BufferedImage image;
    private final EventListenerList listeners = new EventListenerList();
    private final byte[] pixelBuffer = new byte[1];
    private final WritableRaster raster;

    private BinaryImage(int width, int height, @NotNull BufferedImage image) {
        this.image = image;
        raster = image.getRaster();
    }

    public static BinaryImage from(@NotNull BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_BYTE_BINARY) {
            throw new IllegalArgumentException("Image is not binary indexed");
        }

        if (image.getColorModel() instanceof IndexColorModel cm && cm.getMapSize() != 2) {
            throw new IllegalArgumentException("Image is not binary indexed");
        }

        return new BinaryImage(image.getWidth(), image.getHeight(), image);
    }

    public static BinaryImage of(int width, int height, boolean fill) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        var binaryImage = new BinaryImage(width, height, img);
        binaryImage.fill(fill, false);
        return binaryImage;
    }

    public static BinaryImage of(int width, int height) {
        var img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        return new BinaryImage(width, height, img);
    }

    public void addChangeListener(@NotNull BinaryImage.ImageChangeListener listener) {
        listeners.add(ImageChangeListener.class, listener);
    }

    public void draw(@NotNull Graphics2D graphics, int x, int y, int width, int height) {
        graphics.drawImage(image, x, y, width, height, null);
    }

    public void fill(boolean value, boolean notify) {
        var lineBuffer = new byte[image.getWidth()];
        Arrays.fill(lineBuffer, (value ? b1 : b0));
        for (var y = 0; y < image.getHeight(); y++) {
            raster.setDataElements(0, y, image.getWidth(), 1, lineBuffer);
        }
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void fill(boolean value) {
        fill(value, true);
    }

    @Override
    public void flush() {
        image.flush();
        super.flush();
    }

    public boolean get(int x, int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0] == b1;
    }

    @Override
    public float getAccelerationPriority() {
        return image.getAccelerationPriority();
    }

    @Override
    public void setAccelerationPriority(float priority) {
        image.setAccelerationPriority(priority);
    }

    @Override
    public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
        return image.getCapabilities(gc);
    }

    public ColorModel getColorModel() {
        return image.getColorModel();
    }

    public byte[] getDataElements(int x, int y, int width, int height, byte[] target) {
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
    public int getHeight(@Nullable ImageObserver observer) {
        return image.getHeight(observer);
    }

    @Override
    public Class<ImageChangeListener> getListenerClass() {
        return ImageChangeListener.class;
    }

    @Override
    public EventListenerList getListenerList() {
        return listeners;
    }

    @Override
    public Object getProperty(String name, @Nullable ImageObserver observer) {
        return image.getProperty(name, observer);
    }

    public Raster getRaster() {
        return image.getData();
    }

    @Override
    public Image getScaledInstance(int width, int height, int hints) {
        return image.getScaledInstance(width, height, hints);
    }

    public Snapshot getSnapshot(int id) {
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
    public int getWidth(@Nullable ImageObserver observer) {
        return image.getWidth(observer);
    }

    public void invert(int x, int y, boolean notify) {
        set(x, y, !get(x, y));
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void invert(int x, int y) {
        invert(x, y, true);
    }

    public void removeChangeListener(@NotNull BinaryImage.ImageChangeListener listener) {
        listeners.remove(ImageChangeListener.class, listener);
    }

    public void set(int x, int y, boolean value, boolean notify) {
        pixelBuffer[0] = value ? b1 : b0;
        raster.setDataElements(x, y, pixelBuffer);
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void set(int x, int y, boolean value) {
        set(x, y, value, true);
    }

    public void requestUpdate() {
        fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
    }

    public void setDataElements(int x, int y, int width, int height, byte[] source, boolean notify) {
        raster.setDataElements(x, y, width, height, source);
        if (notify) {
            fireChangeEvent(this, ImageChangeEvent.IMAGE_MODIFIED);
        }
    }

    public void setDataElements(int x, int y, int width, int height, byte[] source) {
        setDataElements(x, y, width, height, source, true);
    }

    public byte getByteValue(int x, int y) {
        raster.getDataElements(x, y, pixelBuffer);
        return pixelBuffer[0];
    }

    private void setByteValue(int x, int y, byte value) {
        pixelBuffer[0] = value;
        raster.setDataElements(x, y, pixelBuffer);
    }

    public enum ImageChangeEvent {
        IMAGE_MODIFIED
    }

    public interface ImageChangeListener extends ChangeListener<BinaryImage, ImageChangeEvent> {
    }
}
