package io.github.pixelj.util;

import java.util.EventListener;

public interface ChangeListener<S, E> extends EventListener {
    void onChange(S sender, E event);
}
