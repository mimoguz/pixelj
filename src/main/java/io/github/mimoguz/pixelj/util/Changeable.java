package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;

public interface Changeable<S, A, L extends ChangeListener<S, A>> {
    default void addChangeListener(final L listener) {
        getListenerList().add(getListenerClass(), listener);
    }

    default void fireChangeEvent(final S sender, final A args) {
        final var listeners = getListenerList().getListeners(getListenerClass()).clone();
        for (var i = listeners.length - 1; i >= 0; i--) {
            listeners[i].onChange(sender, args);
        }
    }

    Class<L> getListenerClass();

    EventListenerList getListenerList();

    default void removeChangeListener(final L listener) {
        getListenerList().remove(getListenerClass(), listener);
    }
}
