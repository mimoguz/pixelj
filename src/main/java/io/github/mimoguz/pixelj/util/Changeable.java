package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;

import org.eclipse.jdt.annotation.NonNull;

public interface Changeable<S, E, L extends ChangeListener<S, E>> {
    default void addChangeListener(final L listener) {
        getListenerList().add(getListenerClass(), listener);
    }

    default void fireChangeEvent(final S sender, @NonNull final E event) {
        final var listeners = getListenerList().getListeners(getListenerClass()).clone();
        for (var i = listeners.length - 1; i >= 0; i--) {
            listeners[i].onChange(sender, event);
        }
    }

    @NonNull
    Class<L> getListenerClass();

    @NonNull
    EventListenerList getListenerList();

    default void removeChangeListener(@NonNull final L listener) {
        getListenerList().remove(getListenerClass(), listener);
    }
}
