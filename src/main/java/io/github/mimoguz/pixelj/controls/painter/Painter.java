package io.github.mimoguz.pixelj.controls.painter;

import io.github.mimoguz.pixelj.models.CharacterModel;

import org.jetbrains.annotations.Nullable;

public interface Painter {
    int getHeight();

    @Nullable CharacterModel getModel();

    int getWidth();

    void takeSnapshot();
}
