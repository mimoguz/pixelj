package pixelj.util;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

public final class ChangeableBoolean {

    private final Set<Listener> listeners = new HashSet<>();
    private boolean value;

    public ChangeableBoolean() {
        this(false);
    }

    public ChangeableBoolean(final boolean value) {
        this.value = value;
    }

    /**
     * Keeps strong references to listeners.
     *
     * @param listener Change listener to be added.
     */
    public void addChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        if (value != this.value) {
            this.value = value;
            fireChangeEvent();
        }
    }

    /**
     * @return !this
     */
    public ReadOnlyBoolean not() {
        final var result = new ChangeableBoolean();
        final Listener listener = (sender, a) -> result.setValue(!a);
        addChangeListener(listener);
        return new ReadOnlyBoolean(result, () -> this.removeChangeListener(listener));
    }

    /**
     * @param   that LHS
     * @return  this && that
     */
    public ReadOnlyBoolean and(final ChangeableBoolean that) {
        return binaryOp(that, (a, b) -> a && b);
    }

    /**
     * @param   that LHS
     * @return  this || that
     */
    public ReadOnlyBoolean or(final ChangeableBoolean that) {
        return binaryOp(that, (a, b) -> a || b);
    }

    /**
     * @param listener Change listener to be removed
     */
    public void removeChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private ReadOnlyBoolean binaryOp(final ChangeableBoolean that, final BinaryOperator operator) {
        final var result = new ChangeableBoolean();
        final Listener listenerThis = (sender, a) -> result.setValue(operator.op(a, that.value));
        final Listener listenerThat = (sender, b) -> result.setValue(operator.op(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return new ReadOnlyBoolean(result);
    }

    private void fireChangeEvent() {
        final var lst = listeners.stream().toList();
        for (var listener : lst) {
            listener.onChange(this, value);
        }
    }

    private interface BinaryOperator {
        boolean op(boolean a, boolean b);
    }

    public interface Listener extends EventListener {
        /**
         * @param sender The event source
         * @param value  The new value
         */
        void onChange(ChangeableBoolean sender, boolean value);
    }
}
