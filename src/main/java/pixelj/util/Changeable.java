package pixelj.util;

import java.util.Set;

public interface Changeable<S, A, L extends ChangeListener<S, A>> {

    /**
     * Changeable interface uses strong references to listeners.
     *
     * @param listener Change listener to be added.
     */
    default void addChangeListener(final L listener) {
        if (listener != null) {
            getListeners().add(listener);
        }
    }

    /**
     * Call all listeners.
     *
     * @param sender Event source
     * @param args   Event arguments
     */
    default void fireChangeEvent(final S sender, final A args) {
        final var listeners = getListeners().stream().toList();
        for (var listener : listeners) {
            if (listener != null) {
                listener.onChange(sender, args);
            }
        }
    }

    Set<L> getListeners();

    /**
     * @param listener Change listener to be removed
     */
    default void removeChangeListener(final L listener) {
        if (listener != null) {
            getListeners().remove(listener);
        }
    }
}
