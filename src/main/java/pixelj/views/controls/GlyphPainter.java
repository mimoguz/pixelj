package pixelj.views.controls;

import java.awt.Color;
import java.util.function.Consumer;

import pixelj.graphics.Snapshot;
import pixelj.models.Glyph;
import pixelj.util.ChangeableInt;
import pixelj.views.controls.painter.CanFlipImage;
import pixelj.views.controls.painter.CanRotateImage;
import pixelj.views.controls.painter.CanTranslateImage;
import pixelj.views.controls.painter.PaintAdapter;

public final class GlyphPainter extends GlyphView implements CanFlipImage, CanRotateImage, CanTranslateImage {

    private final ChangeableInt.Listener characterWidthChangeListener;
    private final PaintAdapter paintAdapter;
    private Consumer<Snapshot> snapshotConsumer = snapshot -> { // Ignore
    };

    public GlyphPainter(final Color backgroundColor) {
        super(backgroundColor);
        paintAdapter = new PaintAdapter(this);
        addMouseListener(paintAdapter);
        addMouseMotionListener(paintAdapter);
        characterWidthChangeListener = (sender, value) -> paintAdapter.setExtent(value);
    }

    /** Fill the image all white. */
    public void erase() {
        final var model = getModel();
        if (model != null) {
            model.getImage().fill(true);
        }
    }

    public boolean isSymmetrical() {
        return paintAdapter.isSymmetrical();
    }

    public void setSymmetrical(final boolean value) {
        paintAdapter.setSymmetrical(value);
    }

    @Override
    public void setModel(final Glyph value) {
        final var current = getModel();
        if (current != null) {
            current.widthProperty.removeChangeListener(characterWidthChangeListener);
        }
        if (value != null) {
            paintAdapter.setExtent(value.getWidth());
            value.widthProperty.addChangeListener(characterWidthChangeListener);
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
            snapshotConsumer.accept(model.getImage().getSnapshot(model.getCodePoint()));
        }
    }
}
