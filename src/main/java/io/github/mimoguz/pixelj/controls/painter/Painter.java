package io.github.mimoguz.pixelj.controls.painter;

import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;

public interface Painter {
    CharacterModel getModel();

    void addSnapshot(Snapshot snapshot);

    int getWidth();

    int getHeight();
}
