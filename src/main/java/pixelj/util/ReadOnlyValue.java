package pixelj.util;

import java.util.Set;
import java.util.function.Function;

/**
 * Read-only wrapper for ChangeableValue.
 *
 * @param <T> Value type
 */
public final class ReadOnlyValue<T>
    implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>>, Detachable {

    private final Runnable cleaner;
    private final ChangeableValue<T> delegate;

    public ReadOnlyValue(final ChangeableValue<T> delegate, final Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyValue(final ChangeableValue<T> delegate) {
        this(delegate, null);
    }

    public T getValue() {
        return delegate.getValue();
    }

    @Override
    public Set<ChangeableValue.Listener<T>> getListeners() {
        return delegate.getListeners();
    }

    @Override
    public void detach() {
        if (cleaner != null) {
            cleaner.run();
        }
    }

    /**
     * @see ChangeableValue#map(Function)
     *
     * @param <U>      Destination type
     * @param function Mapping from T to U
     * @return ReadOnlyValue that holds the mapped value
     */
    public <U> ReadOnlyValue<U> map(final Function<T, U> function) {
        return delegate.map(function);
    }
}
