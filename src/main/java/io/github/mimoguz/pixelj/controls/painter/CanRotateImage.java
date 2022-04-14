package io.github.mimoguz.pixelj.controls.painter;

import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;

public interface CanRotateImage extends Painter {
    /**
     * @param model Character model. Skips the null check.
     * @param getX  (top-left square size, y) -> target x
     * @param getY  (top-left square size, x) -> target y
     * @return Snapshot before the change
     */
    private static Snapshot rotate(CharacterModel model, IntBinaryOperator getX, IntBinaryOperator getY) {
        final var image = model.getGlyph();
        final var snapshot = image.getSnapshot(model.getCodePoint());
        final var width = image.getWidth();
        final var size = Math.min(image.getWidth(), image.getHeight());
        final var buffer = new byte[image.getWidth() * image.getHeight()];
        Arrays.fill(buffer, (byte) 1);

        for (var y = 0; y < size; y++) {
            for (var x = 0; x < size; x++) {
                final var destX = getX.applyAsInt(size, y);
                final var destY = getY.applyAsInt(size, x);
                buffer[destY * width + destX] = image.getByteValue(x, y);
            }
        }

        image.setDataElements(0, 0, width, image.getHeight(), buffer, true);

        return snapshot;
    }

    /**
     * Rotates the top-left square of the image 90 degrees left, and fills the rest
     * with 1.
     */
    default void rotateLeft() {
        final var model = getModel();
        if (model == null) {
            return;
        }
        addSnapshot(rotate(model, (size, y) -> y, (size, x) -> size - x - 1));
    }

    /**
     * Rotates the top-left square of the 90 degrees right, and fills the rest with
     * 1.
     */
    default void rotateRight() {
        final var model = getModel();
        if (model == null) {
            return;
        }
        addSnapshot(rotate(getModel(), (size, y) -> size - y - 1, (size, x) -> x));
    }
}
