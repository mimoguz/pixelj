package io.github.mimoguz.pixelj.views.controls.painter;

import io.github.mimoguz.pixelj.models.CharacterItem;

public interface Painter {
    int getHeight();

    CharacterItem getModel();

    int getWidth();

    void takeSnapshot();
}
