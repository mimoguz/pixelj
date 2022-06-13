package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;

public class MutableValue<T> implements Changeable<MutableValue<T>, T, MutableValue.Listener<T>> {
    private final Class<Listener<T>> cls;
    private final EventListenerList listeners = new EventListenerList();
    private final T value;

    @SuppressWarnings("unchecked")
    public <U extends Listener<T>> MutableValue(Class<U> cls, T value) {
        this.cls = (Class<Listener<T>>) cls;
        this.value = value;
    }

    @Override
    public Class<Listener<T>> getListenerClass() {
        return cls;
    }

    @Override
    public EventListenerList getListenerList() {
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
