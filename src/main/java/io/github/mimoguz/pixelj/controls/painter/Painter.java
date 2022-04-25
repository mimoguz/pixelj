package io.github.mimoguz.pixelj.controls.painter;

import org.eclipse.jdt.annotation.Nullable;

import io.github.mimoguz.pixelj.models.CharacterModel;

public interface Painter {
    int getHeight();

    @Nullable
    CharacterModel getModel();

    int getWidth();

    void takeSnapshot();
}
