package io.github.mimoguz.pixelj.controls;

import io.github.mimoguz.pixelj.controls.painter.*;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class GlyphPainter
        extends GlyphView
        implements Painter, CanFlipImage, CanRotateImage, CanTranslateImage {

    private final PaintAdapter paintAdapter;
    private Consumer<Snapshot> snapshotConsumer = snapshot -> {
    };

    public GlyphPainter(@NotNull Color backgroundColor) {
        super(backgroundColor);
        paintAdapter = new PaintAdapter(this);
        addMouseListener(paintAdapter);
        addMouseMotionListener(paintAdapter);
    }

    public void addSnapshot(Snapshot snapshot) {
        snapshotConsumer.accept(snapshot);
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

    public void setSymmetrical(boolean value) {
        paintAdapter.setSymmetrical(value);
    }

    @Override
    public void setModel(@Nullable CharacterModel value) {
        if (value != null) {
            paintAdapter.setExtent(value.getWidth());
        }
        super.setModel(value);
    }

    public void setSnapshotConsumer(@NotNull Consumer<Snapshot> value) {
        snapshotConsumer = value;
    }
}