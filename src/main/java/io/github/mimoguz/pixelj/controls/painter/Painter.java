package io.github.mimoguz.pixelj.controls.painter;

import io.github.mimoguz.pixelj.models.CharacterModel;

public interface Painter {
    int getHeight();

    CharacterModel getModel();

    int getWidth();

    void takeSnapshot();
}
