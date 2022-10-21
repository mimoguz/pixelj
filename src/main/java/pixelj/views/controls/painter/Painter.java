package pixelj.views.controls.painter;

import pixelj.graphics.Snapshot;
import pixelj.models.Glyph;

public interface Painter {

    int getHeight();

    Glyph getModel();

    int getWidth();

    /**
     * Take a snapshot of the current state.
     * @return A snapshot, or null if the model is null
     */
    Snapshot takeSnapshot();
}
