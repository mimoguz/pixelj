package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

public class ChangeableBoolean {
    private final EventListenerList listeners = new EventListenerList();
    private boolean value;

    public ChangeableBoolean() {
        this(false);
    }

    public ChangeableBoolean(boolean value) {
        this.value = value;
    }

    public void addChangeListener(final BooleanChangeListener listener) {
        listeners.add(BooleanChangeListener.class, listener);
    }

    public ChangeableBoolean and(ChangeableBoolean that) {
        return binaryOp(that, (a, b) -> a && b);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
        fireChangeEvent();
    }

    public ChangeableBoolean not() {
        final var result = new ChangeableBoolean();
        final BooleanChangeListener listener = (sender, a) -> result.setValue(!a);
        addChangeListener(listener);
        return result;
    }

    public ChangeableBoolean or(ChangeableBoolean that) {
        return binaryOp(that, (a, b) -> a || b);
    }

    public void removeChangeListener(final BooleanChangeListener listener) {
        listeners.remove(BooleanChangeListener.class, listener);
    }

    private ChangeableBoolean binaryOp(ChangeableBoolean that, BinaryOperator operator) {
        final var result = new ChangeableBoolean();
        final BooleanChangeListener listenerThis = (sender, a) -> result.setValue(operator.op(a, that.value));
        final BooleanChangeListener listenerThat = (sender, b) -> result.setValue(operator.op(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return result;
    }

    private void fireChangeEvent() {
        final var lst = listeners.getListeners(BooleanChangeListener.class).clone();
        for (var i = lst.length - 1; i >= 0; i--) {
            lst[i].onChange(this, value);
        }
    }

    private interface BinaryOperator {
        boolean op(boolean a, boolean b);
    }

    public interface BooleanChangeListener extends EventListener {
        void onChange(ChangeableBoolean sender, boolean value);
    }
}