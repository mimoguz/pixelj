package io.github.mimoguz.pixelj.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ChangeableValue<T> implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>> {
    private final Set<Listener<T>> listeners = new HashSet<>();
    private T value;

    public ChangeableValue(T value) {
        this.value = value;
    }

    @Override
    public Set<Listener<T>> getListeners() {
        return listeners;
    }

    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        this.value = value;
        fireChangeEvent(this, value);
    }

    public <U> ReadOnlyValue<U> map(Function<T, U> function) {
        final var result = new ChangeableValue<>(function.apply(value));
        final ChangeableValue.Listener<T> listener = (sender, value) -> result.setValue(function.apply(value));
        return new ReadOnlyValue<>(result, () -> removeChangeListener(listener));
    }

    public interface Listener<T> extends ChangeListener<ChangeableValue<T>, T> {
        // Empty
    }
}
