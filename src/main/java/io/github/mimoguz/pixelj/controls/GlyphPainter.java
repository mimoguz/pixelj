package io.github.mimoguz.pixelj.controls;

import java.awt.Color;
import java.util.function.Consumer;

import io.github.mimoguz.pixelj.controls.painter.*;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.IntValueChangeListener;

public class GlyphPainter extends GlyphView
        implements
        Painter,
        CanFlipImage,
        CanRotateImage,
        CanTranslateImage {

    private static final long serialVersionUID = 2382126540536314203L;

    private final transient IntValueChangeListener characterWidthChangeListener;
    private final transient PaintAdapter paintAdapter;
    private transient Consumer<Snapshot> snapshotConsumer = snapshot -> {
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

    @Override
    public void setModel(final CharacterModel value) {
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

    public void setSymmetrical(final boolean value) {
        paintAdapter.setSymmetrical(value);
    }

    @Override
    public void takeSnapshot() {
        final var model = getModel();
        if (model != null) {
            snapshotConsumer.accept(model.getGlyph().getSnapshot(model.getCodePoint()));
        }
    }
}
