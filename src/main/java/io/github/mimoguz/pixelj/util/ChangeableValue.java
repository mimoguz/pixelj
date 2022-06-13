package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;

public class ChangeableValue<T> implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>> {
    private final Class<Listener<T>> cls;
    private final EventListenerList listeners = new EventListenerList();
    private T value;

    @SuppressWarnings("unchecked")
    public <U extends Listener<T>> ChangeableValue(Class<U> cls) {
        this.cls = (Class<Listener<T>>) cls;
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

    public void setValue(final T value) {
        final var oldValue = this.value;
        this.value = value;
        fireChangeEvent(this, value);
    }

    public interface Listener<T> extends ChangeListener<ChangeableValue<T>, T> {
        // Empty
    }
}
