package pixelj.views.controls.painter;

import pixelj.models.Glyph;

public interface Painter {

    int getHeight();

    Glyph getModel();

    int getWidth();

    /**
     * Take a snapshot of the current state.
     */
    void takeSnapshot();
}
