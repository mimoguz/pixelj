package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;

public class MutableValue<T> implements Changeable<MutableValue<T>, MutableValue.ValueChangeArgs<T>, MutableValue.ValueChangeListener<T>> {
    private final Class<ValueChangeListener<T>> cls;
    private final EventListenerList listeners = new EventListenerList();
    private T value;

    @SuppressWarnings("unchecked")
    public <U extends ValueChangeListener<T>> MutableValue(Class<U> cls) {
        this.cls = (Class<ValueChangeListener<T>>) cls;
    }

    @Override
    public Class<ValueChangeListener<T>> getListenerClass() {
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
        fireChangeEvent(this, new ValueChangeArgs.Changed<T>(value));
    }

    public void informMutation() {
        fireChangeEvent(this, new ValueChangeArgs.Mutated<T>());
    }

    public sealed interface ValueChangeArgs<U> permits ValueChangeArgs.Mutated, ValueChangeArgs.Changed {

        record Changed<U>(U value) implements ValueChangeArgs<U> {
        }

        record Mutated<U>() implements ValueChangeArgs<U> {
        }
    }

    public interface ValueChangeListener<T> extends ChangeListener<MutableValue<T>, MutableValue.ValueChangeArgs<T>> {
        // Empty
    }
}
