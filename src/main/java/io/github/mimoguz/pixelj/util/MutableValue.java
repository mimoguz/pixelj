package io.github.mimoguz.pixelj.util;

import java.util.WeakHashMap;

public class MutableValue<T> implements Changeable<MutableValue<T>, T, MutableValue.Listener<T>> {
    private final WeakHashMap<Listener<T>, Void> listeners = new WeakHashMap<>();
    private final T value;

    public <U extends Listener<T>> MutableValue(T value) {
        this.value = value;
    }

    @Override
    public WeakHashMap<Listener<T>, Void> getListeners() {
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
