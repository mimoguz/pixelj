package pixelj.views.controls.painter;

import pixelj.models.CharacterItem;

public interface Painter {
    int getHeight();

    CharacterItem getModel();

    int getWidth();

    void takeSnapshot();
}
