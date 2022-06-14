package io.github.mimoguz.pixelj.util;

import java.util.WeakHashMap;

public interface Changeable<S, A, L extends ChangeListener<S, A>> {
    default void addChangeListener(final L listener) {
        getListeners().put(listener, null);
    }

    default void fireChangeEvent(final S sender, final A args) {
        final var listeners = getListeners().keySet();
        for (var listener : listeners) {
            listener.onChange(sender, args);
        }
    }

    WeakHashMap<L, Object> getListeners();

    default void removeChangeListener(final L listener) {
        getListeners().remove(listener);
    }
}
