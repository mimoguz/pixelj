package io.github.mimoguz.pixelj.util;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

public class ChangeableInt {
    private final EventListenerList listeners = new EventListenerList();
    private int value;

    public ChangeableInt() {
        this(0);
    }

    public ChangeableInt(int value) {
        this.value = value;
    }

    public ChangeableInt add(ChangeableInt that) {
        return binaryOp(that, Integer::sum);
    }

    public void addChangeListener(final Listener listener) {
        listeners.add(Listener.class, listener);
    }

    public ChangeableInt divide(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a / b);
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
        fireChangeEvent();
    }

    public ChangeableInt multiply(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a * b);
    }

    public ChangeableInt negate() {
        final var result = new ChangeableInt();
        final Listener listener = (sender, a) -> result.setValue(-a);
        addChangeListener(listener);
        return result;
    }

    public void removeChangeListener(final Listener listener) {
        listeners.remove(Listener.class, listener);
    }

    public ChangeableInt subtract(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a - b);
    }

    private ChangeableInt binaryOp(ChangeableInt that, BinaryOperator operator) {
        final var result = new ChangeableInt();
        final Listener listenerThis = (sender, a) -> result.setValue(operator.op(a, that.value));
        final Listener listenerThat = (sender, b) -> result.setValue(operator.op(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return result;
    }

    private void fireChangeEvent() {
        final var lst = listeners.getListeners(Listener.class).clone();
        for (var i = lst.length - 1; i >= 0; i--) {
            lst[i].onChange(this, value);
        }
    }

    private interface BinaryOperator {
        int op(int a, int b);
    }

    public interface Listener extends EventListener {
        void onChange(ChangeableInt sender, int value);
    }
}