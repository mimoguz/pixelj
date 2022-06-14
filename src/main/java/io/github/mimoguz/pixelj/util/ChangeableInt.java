package io.github.mimoguz.pixelj.util;

import java.util.EventListener;
import java.util.WeakHashMap;

public class ChangeableInt {
    private final WeakHashMap<Listener, Void> listeners = new WeakHashMap<>();
    private int value;

    public ChangeableInt() {
        this(0);
    }

    public ChangeableInt(int value) {
        this.value = value;
    }

    public ReadOnlyInt add(ChangeableInt that) {
        return binaryOp(that, Integer::sum);
    }

    public void addChangeListener(final Listener listener) {
        listeners.put(listener, null);
    }

    public ReadOnlyInt divide(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a / b);
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
        fireChangeEvent();
    }

    public ReadOnlyInt multiply(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a * b);
    }

    public ReadOnlyInt negate() {
        final var result = new ChangeableInt();
        final Listener listener = (sender, a) -> result.setValue(-a);
        addChangeListener(listener);
        return new ReadOnlyInt(result);
    }

    public void removeChangeListener(final Listener listener) {
        listeners.remove(listener);
    }

    public ReadOnlyInt subtract(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a - b);
    }

    private ReadOnlyInt binaryOp(ChangeableInt that, BinaryOperator operator) {
        final var result = new ChangeableInt();
        final Listener listenerThis = (sender, a) -> result.setValue(operator.op(a, that.value));
        final Listener listenerThat = (sender, b) -> result.setValue(operator.op(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return new ReadOnlyInt(result);
    }

    private void fireChangeEvent() {
        final var lst = listeners.keySet();
        for (var listener : lst) {
            listener.onChange(this, value);
        }
    }

    private interface BinaryOperator {
        int op(int a, int b);
    }

    public interface Listener extends EventListener {
        void onChange(ChangeableInt sender, int value);
    }
}