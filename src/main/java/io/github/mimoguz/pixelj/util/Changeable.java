package io.github.mimoguz.pixelj.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.event.EventListenerList;

@ParametersAreNonnullByDefault
public interface Changeable<S, E, L extends ChangeListener<S, E>> {
    default void addChangeListener(final L listener) {
        getListenerList().add(getListenerClass(), listener);
    }

    default void fireChangeEvent(final S sender, final E event) {
        final var listeners = getListenerList().getListeners(getListenerClass()).clone();
        for (var i = listeners.length - 1; i >= 0; i--) {
            listeners[i].onChange(sender, event);
        }
    }

    @NotNull Class<L> getListenerClass();

    @NotNull EventListenerList getListenerList();

    default void removeChangeListener(final L listener) {
        getListenerList().remove(getListenerClass(), listener);
    }
}
