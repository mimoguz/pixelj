package pixelj.util;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

public class ChangeableBoolean {
    private final Set<Listener> listeners = new HashSet<>();
    private boolean value;

    public ChangeableBoolean() {
        this(false);
    }

    public ChangeableBoolean(boolean value) {
        this.value = value;
    }

    public void addChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public ReadOnlyBoolean and(ChangeableBoolean that) {
        return binaryOp(that, (a, b) -> a && b);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
        fireChangeEvent();
    }

    public ReadOnlyBoolean not() {
        final var result = new ChangeableBoolean();
        final Listener listener = (sender, a) -> result.setValue(!a);
        addChangeListener(listener);
        return new ReadOnlyBoolean(result, () -> this.removeChangeListener(listener));
    }

    public ReadOnlyBoolean or(ChangeableBoolean that) {
        return binaryOp(that, (a, b) -> a || b);
    }

    public void removeChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private ReadOnlyBoolean binaryOp(ChangeableBoolean that, BinaryOperator operator) {
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
        void onChange(ChangeableBoolean sender, boolean value);
    }
}