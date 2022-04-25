package io.github.mimoguz.pixelj.controls;

import java.awt.Color;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import io.github.mimoguz.pixelj.controls.painter.CanFlipImage;
import io.github.mimoguz.pixelj.controls.painter.CanRotateImage;
import io.github.mimoguz.pixelj.controls.painter.CanTranslateImage;
import io.github.mimoguz.pixelj.controls.painter.PaintAdapter;
import io.github.mimoguz.pixelj.controls.painter.Painter;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;

public class GlyphPainter extends GlyphView
        implements
        Painter,
        CanFlipImage,
        CanRotateImage,
        CanTranslateImage {

    private static final long serialVersionUID = 2382126540536314203L;

    private final PaintAdapter paintAdapter;

    private Consumer<Snapshot> snapshotConsumer = snapshot -> {
    };

    public GlyphPainter(final Color backgroundColor) {
        super(backgroundColor);
        paintAdapter = new PaintAdapter(this);
        addMouseListener(paintAdapter);
        addMouseMotionListener(paintAdapter);
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
    public void setModel(@Nullable final CharacterModel value) {
        if (value != null) {
            paintAdapter.setExtent(value.getWidth());
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
