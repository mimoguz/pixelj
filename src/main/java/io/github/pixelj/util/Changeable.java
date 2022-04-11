package io.github.pixelj.util;

import javax.swing.event.EventListenerList;

public interface Changeable<S, E, L extends ChangeListener<S, E>> {
    EventListenerList getListenerList();

    Class<L> getListenerClass();

    default void addListener(L listener) {
        getListenerList().add(getListenerClass(), listener);
    }

    default void removeListener(L listener) {
        getListenerList().remove(getListenerClass(), listener);
    }

    default void fireChangeEvent(S sender, E event) {
        final var listeners = getListenerList().getListeners(getListenerClass()).clone();
        for (var i = listeners.length - 1; i >= 0; i--) {
            listeners[i].onChange(sender, event);
        }
    }
}
