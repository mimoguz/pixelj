package io.github.pixelj.controls.painter;

import io.github.pixelj.graphics.Snapshot;
import io.github.pixelj.models.CharacterModel;

public interface Painter {
    CharacterModel getModel();

    void addSnapshot(Snapshot snapshot);

    int getWidth();

    int getHeight();
}
