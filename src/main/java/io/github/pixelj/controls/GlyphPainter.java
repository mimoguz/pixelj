package io.github.pixelj.controls;

import io.github.pixelj.controls.painter.*;
import io.github.pixelj.graphics.Snapshot;
import io.github.pixelj.models.CharacterModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class GlyphPainter
        extends GlyphView
        implements
        Painter,
        CanFlipImage,
        CanRotateImage,
        CanTranslateImage {

    private Consumer<Snapshot> snapshotConsumer = snapshot -> {
    };

    private final PaintAdapter paintAdapter;

    public GlyphPainter(@NotNull Color backgroundColor) {
        super(backgroundColor);
        paintAdapter = new PaintAdapter(this);
        addMouseListener(paintAdapter);
        addMouseMotionListener(paintAdapter);
    }

    @Override
    public void setModel(@Nullable CharacterModel value) {
        if (value != null) {
            paintAdapter.setExtent(value.getWidth());
        }
        super.setModel(value);
    }

    public void addSnapshot(Snapshot snapshot) {
        snapshotConsumer.accept(snapshot);
    }

    public void setSnapshotConsumer(@NotNull Consumer<Snapshot> value) {
        snapshotConsumer = value;
    }
}
