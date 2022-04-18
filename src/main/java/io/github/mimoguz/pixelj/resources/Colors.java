package io.github.mimoguz.pixelj.resources;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface Colors {
    @NotNull Color accent();

    @NotNull Color active();

    @NotNull Color disabledIcon();

    @NotNull Color divider();

    @NotNull Color faintIcon();

    @NotNull Color focusBackground();

    @NotNull Color focusForeground();

    @NotNull Color icon();

    @NotNull Color text();
}
