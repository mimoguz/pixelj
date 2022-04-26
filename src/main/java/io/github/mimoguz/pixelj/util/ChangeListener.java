package io.github.mimoguz.pixelj.util;

import java.util.EventListener;

import org.eclipse.jdt.annotation.NonNull;

public interface ChangeListener<S, E> extends EventListener {
    void onChange(S sender, @NonNull E event);
}
