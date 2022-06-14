package io.github.mimoguz.pixelj.util;

import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class ChangeableValue<T> implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>> {
    private final WeakHashMap<Listener<T>, Void> listeners = new WeakHashMap<>();
    private T value;

    public ChangeableValue(T value) {
        this.value = value;
    }

    @Override
    public WeakHashMap<Listener<T>, Void> getListeners() {
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
        return new ReadOnlyValue<>(result);
    }

    public <U> ReadOnlyBoolean mapToBoolean(Predicate<T> function) {
        final var result = new ChangeableBoolean(function.test(value));
        return new ReadOnlyBoolean(result);
    }

    public <U> ReadOnlyInt mapToInt(ToIntFunction<T> function) {
        final var result = new ChangeableInt(function.applyAsInt(value));
        return new ReadOnlyInt(result);
    }

    public interface Listener<T> extends ChangeListener<ChangeableValue<T>, T> {
        // Empty
    }
}
