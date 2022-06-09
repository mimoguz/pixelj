package io.github.mimoguz.pixelj.controls;

import io.github.mimoguz.pixelj.controls.painter.*;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterItem;
import io.github.mimoguz.pixelj.models.IntValueChangeListener;

import java.awt.*;
import java.util.function.Consumer;

public class GlyphPainter extends GlyphView
        implements
        Painter,
        CanFlipImage,
        CanRotateImage,
        CanTranslateImage {

    private final IntValueChangeListener characterWidthChangeListener;
    private final PaintAdapter paintAdapter;
    private Consumer<Snapshot> snapshotConsumer = snapshot -> { // Ignore
    };

    public GlyphPainter(final Color backgroundColor) {
        super(backgroundColor);
        paintAdapter = new PaintAdapter(this);
        addMouseListener(paintAdapter);
        addMouseMotionListener(paintAdapter);
        characterWidthChangeListener = (sender, event) -> paintAdapter.setExtent(event.newValue());
    }

    public void erase() {
        final var model = getModel();
        if (model != null) {
            model.getGlyph().fill(true);
        }
    }

    public boolean isSymmetrical() {
        return paintAdapter.isSymmetrical();
    }

    public void setSymmetrical(final boolean value) {
        paintAdapter.setSymmetrical(value);
    }

    @Override
    public void setModel(final CharacterItem value) {
        final var current = getModel();
        if (current != null) {
            current.removeChangeListener(characterWidthChangeListener);
        }
        if (value != null) {
            paintAdapter.setExtent(value.getWidth());
            value.addChangeListener(characterWidthChangeListener);
        }
        super.setModel(value);
    }

    public void setSnapshotConsumer(final Consumer<Snapshot> value) {
        snapshotConsumer = value;
    }

    @Override
    public void takeSnapshot() {
        final var model = getModel();
        if (model != null) {
            snapshotConsumer.accept(model.getGlyph().getSnapshot(model.getCodePoint()));
        }
    }
}
