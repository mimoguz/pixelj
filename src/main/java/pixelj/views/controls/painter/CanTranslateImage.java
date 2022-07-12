package pixelj.views.controls.painter;

import java.util.Arrays;

public interface CanTranslateImage extends Painter {

    /**
     * Moves the image one pixel left or right.
     *
     * @param sourceX First column of the source region. If it's 0, the region will
     *                be moved to the right. If it's 1, the region will be moved to
     *                the left.
     */
    private void moveHorizontally(int sourceX) {
        final var model = getModel();
        if (model == null) {
            return;
        }

        takeSnapshot();

        final var image = model.getImage();
        final var buffer = new byte[image.getImageWidth()];
        final var length = image.getImageWidth() - 1;
        final var height = image.getImageHeight();

        for (var y = 0; y < height; y++) {
            image.getDataElements(sourceX, y, length, 1, buffer);
            // Suppress notifications
            image.setDataElements(1 - sourceX, y, length, 1, buffer, false);
            image.set(length * sourceX, y, true, false);
        }

        // Trigger change notification
        image.requestUpdate();
    }

    /**
     * Moves the image one pixel down, and fills the first row with 1.
     */
    default void moveOnePixelDown() {
        final var model = getModel();
        if (model == null) {
            return;
        }

        takeSnapshot();

        final var image = model.getImage();
        final var buffer = new byte[image.getImageWidth()];
        final var width = image.getImageWidth();

        for (var y = image.getImageHeight() - 2; y >= 0; y--) {
            image.getDataElements(0, y, width, 1, buffer);
            // Suppress notifications
            image.setDataElements(0, y + 1, width, 1, buffer, false);
        }

        Arrays.fill(buffer, (byte) 1);
        image.setDataElements(0, 0, width, 1, buffer, false);

        // Trigger change notification
        image.requestUpdate();
    }

    /**
     * Moves the image one pixel left, and fills the last column with 1.
     */
    default void moveOnePixelLeft() {
        moveHorizontally(1);
    }

    /**
     * Moves the image one pixel right, and fills the first column with 1.
     */
    default void moveOnePixelRight() {
        moveHorizontally(0);
    }

    /**
     * Moves the image one pixel up, and fills the last row with 1.
     */
    default void moveOnePixelUp() {
        final var model = getModel();
        if (model == null) {
            return;
        }

        takeSnapshot();

        final var image = model.getImage();
        final var buffer = new byte[image.getImageWidth()];
        final var width = image.getImageWidth();
        final var height = image.getImageHeight();

        for (var y = 1; y < height; y++) {
            image.getDataElements(0, y, width, 1, buffer);
            // Suppress notifications
            image.setDataElements(0, y - 1, width, 1, buffer, false);
        }

        Arrays.fill(buffer, (byte) 1);
        image.setDataElements(0, height - 1, width, 1, buffer, false);

        // Trigger change notification
        image.requestUpdate();
    }
}
