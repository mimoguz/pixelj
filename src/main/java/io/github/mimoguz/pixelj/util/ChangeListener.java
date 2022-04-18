package io.github.mimoguz.pixelj.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

public interface ChangeListener<S, E> extends EventListener {
    void onChange(@Nullable S sender, @NotNull E event);
}
