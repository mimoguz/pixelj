package pixelj.views.controls.painter;

public interface CanFlipImage extends Painter {

    /** Reverses the byte array in-place.
    *
    * @param array Input
    */
    private static void reverseArray(final byte[] array) {
        final var half = array.length / 2;
        for (var left = 0; left < half; left++) {
            final var temp = array[left];
            final var right = array.length - left - 1;
            array[left] = array[right];
            array[right] = temp;
        }
    }

    /** Flips the image along the x-axis. */
    default void flipHorizontally() {
        final var model = getModel();
        if (model == null) {
            return;
        }

        takeSnapshot();

        final var image = model.getImage();
        final var width = image.getImageWidth();
        final var height = image.getImageHeight();
        final var buffer = new byte[width];

        for (var y = 0; y < height; y++) {
            image.getDataElements(0, y, width, 1, buffer);
            reverseArray(buffer);
            // Suppress notifications
            image.setDataElements(0, y, width, 1, buffer, false);
        }

        // Trigger change notification
        image.requestUpdate();
    }

    /** Flips the image along the y-axis. */
    default void flipVertically() {
        final var model = getModel();
        if (model == null) {
            return;
        }

        takeSnapshot();

        final var image = model.getImage();
        final var width = image.getImageWidth();
        final var height = image.getImageHeight();
        final var half = height / 2;
        final var top = new byte[width];
        final var bottom = new byte[width];

        for (var y = 0; y < half; y++) {
            final var bottomY = height - y - 1;
            image.getDataElements(0, y, width, 1, top);
            image.getDataElements(0, bottomY, width, 1, bottom);
            // Suppress notifications
            image.setDataElements(0, y, width, 1, bottom, false);
            image.setDataElements(0, bottomY, width, 1, top, false);
        }

        // Trigger change notification
        image.requestUpdate();
    }
}
