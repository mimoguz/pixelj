package io.github.mimoguz.pixelj.util;

import java.util.Set;
import java.util.function.Function;

public class ReadOnlyValue<T> implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>> {
    private final ChangeableValue<T> delegate;

    public ReadOnlyValue(ChangeableValue<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<ChangeableValue.Listener<T>> getListeners() {
        return delegate.getListeners();
    }

    public T getValue() {
        return delegate.getValue();
    }

    public <U> ReadOnlyValue<U> map(Function<T, U> function) {
        final var result = new ChangeableValue<>(function.apply(delegate.getValue()));
        return new ReadOnlyValue<>(result);
    }
}