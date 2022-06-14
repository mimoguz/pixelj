package pixelj.util;

import java.util.Set;
import java.util.function.Function;

public class ReadOnlyValue<T> implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>>, Detachable {
    private final Runnable cleaner;
    private final ChangeableValue<T> delegate;

    public ReadOnlyValue(ChangeableValue<T> delegate, Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyValue(ChangeableValue<T> delegate) {
        this(delegate, null);
    }

    @Override
    public void detach() {
        if (cleaner != null) {
            cleaner.run();
        }
    }

    @Override
    public Set<ChangeableValue.Listener<T>> getListeners() {
        return delegate.getListeners();
    }

    public T getValue() {
        return delegate.getValue();
    }

    public <U> ReadOnlyValue<U> map(Function<T, U> function) {
        return delegate.map(function);
    }
}