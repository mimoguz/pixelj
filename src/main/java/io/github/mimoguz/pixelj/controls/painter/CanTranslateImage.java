package io.github.mimoguz.pixelj.controls.painter;

import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public interface CanTranslateImage extends Painter {

    /**
     * Moves the image one pixel left or right
     *
     * @param model   Character model. Skips the null check.
     * @param sourceX First column of the source region. If it's 0, the region will
     *                be moved to the right. If it's 1, the region will be moved to
     *                the left.
     * @return Snapshot before the change
     */
    private static Snapshot moveHorizontally(@NotNull CharacterModel model, int sourceX) {
        final var image = model.getGlyph();
        final var snapshot = image.getSnapshot(model.getCodePoint());
        final var buffer = new byte[image.getWidth()];
        final var length = image.getWidth() - 1;
        final var height = image.getHeight();

        for (var y = 0; y < height; y++) {
            image.getDataElements(sourceX, y, length, 1, buffer);
            // Suppress notifications
            image.setDataElements(1 - sourceX, y, length, 1, buffer, false);
            image.set(length * sourceX, y, true, false);
        }

        // Trigger change notification
        image.requestUpdate();

        return snapshot;
    }

    /**
     * Moves the image one pixel down, and fills the first row with 1.
     */
    default void moveOnePixelDown() {
        final var model = getModel();
        if (model == null) {
            return;
        }

        final var image = model.getGlyph();
        addSnapshot(image.getSnapshot(model.getCodePoint()));

        final var buffer = new byte[image.getWidth()];
        final var width = image.getWidth();

        for (var y = image.getHeight() - 2; y >= 0; y--) {
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
        final var model = getModel();
        if (model == null) {
            return;
        }
        addSnapshot(moveHorizontally(model, 1));
    }

    /**
     * Moves the image one pixel right, and fills the first column with 1.
     */
    default void moveOnePixelRight() {
        final var model = getModel();
        if (model == null) {
            return;
        }
        addSnapshot(moveHorizontally(model, 0));
    }

    /**
     * Moves the image one pixel up, and fills the last row with 1.
     */
    default void moveOnePixelUp() {
        final var model = getModel();
        if (model == null) {
            return;
        }

        final var image = model.getGlyph();
        addSnapshot(image.getSnapshot(model.getCodePoint()));

        final var buffer = new byte[image.getWidth()];
        final var width = image.getWidth();
        final var height = image.getHeight();

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
