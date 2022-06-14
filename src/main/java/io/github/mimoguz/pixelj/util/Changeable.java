package io.github.mimoguz.pixelj.util;

import java.util.WeakHashMap;

public interface Changeable<S, A, L extends ChangeListener<S, A>> {
    /**
     * Changeable interface uses weak references. Make sure you're keeping another reference to your listener.
     */
    default void addChangeListener(final L listener) {
        getListeners().put(listener, null);
    }

    default void fireChangeEvent(final S sender, final A args) {
        final var listeners = getListeners().keySet();
        for (var listener : listeners) {
            listener.onChange(sender, args);
        }
    }

    WeakHashMap<L, Void> getListeners();

    default void removeChangeListener(final L listener) {
        getListeners().remove(listener);
    }
}
