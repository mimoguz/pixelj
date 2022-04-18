package io.github.mimoguz.pixelj.util;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EventListener;

@ParametersAreNonnullByDefault
public interface ChangeListener<S, E> extends EventListener {
    void onChange(@Nullable S sender, E event);
}
