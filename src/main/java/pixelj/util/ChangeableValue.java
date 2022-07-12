package pixelj.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ChangeableValue<T>
        implements Changeable<ChangeableValue<T>, T, ChangeableValue.Listener<T>> {

    private final Set<Listener<T>> listeners = new HashSet<>();
    private T value;

    public ChangeableValue(final T value) {
        this.value = value;
    }

    @Override
    public Set<Listener<T>> getListeners() {
        return listeners;
    }

    public T getValue() {
        return value;
    }

    /**
     * @param value The new value. Tests for reference equality before setting.
     */
    public void setValue(final T value) {
        if (this.value != value) {
            this.value = value;
            fireChangeEvent(this, value);
        }
    }

    /**
     * @param <U>      Destination type
     * @param function Mapping from T to U
     * @return ReadOnlyValue that holds the mapped value
     */
    public <U> ReadOnlyValue<U> map(final Function<T, U> function) {
        final var result = new ChangeableValue<>(function.apply(value));
        final ChangeableValue.Listener<T> listener = (sender, v) -> result.setValue(function.apply(v));
        this.addChangeListener(listener);
        return new ReadOnlyValue<>(result, () -> removeChangeListener(listener));
    }

    /**
     * @param predicate
     * @return ReadOnlyBoolean that holds predicate test result
     */
    public ReadOnlyBoolean test(final Predicate<T> predicate) {
        final var result = new ChangeableBoolean(predicate.test(value));
        final ChangeableValue.Listener<T> listener = (sender, v) -> result.setValue(predicate.test(v));
        this.addChangeListener(listener);
        return new ReadOnlyBoolean(result, () -> removeChangeListener(listener));
    }

    public interface Listener<T> extends ChangeListener<ChangeableValue<T>, T> {
        // Empty
    }
}
