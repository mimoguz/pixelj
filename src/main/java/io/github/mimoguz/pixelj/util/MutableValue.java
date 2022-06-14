package io.github.mimoguz.pixelj.util;

import java.util.HashSet;
import java.util.Set;

public class MutableValue<T> implements Changeable<MutableValue<T>, T, MutableValue.Listener<T>> {
    private final Set<Listener<T>> listeners = new HashSet<>();
    private final T value;

    public <U extends Listener<T>> MutableValue(T value) {
        this.value = value;
    }

    @Override
    public Set<Listener<T>> getListeners() {
        return listeners;
    }

    public T getValue() {
        return value;
    }

    public void informMutation() {
        fireChangeEvent(this, value);
    }

    public interface Listener<T> extends ChangeListener<MutableValue<T>, T> {
        // Empty
    }
}
