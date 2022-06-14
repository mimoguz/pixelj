package io.github.mimoguz.pixelj.util;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

public class ChangeableInt {
    private final Set<Listener> listeners = new HashSet<>();
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
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public ReadOnlyInt divide(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a / b);
    }

    public ReadOnlyBoolean ge(ChangeableInt that) {
        return comparison(that, (a, b) -> a >= b);
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
        fireChangeEvent();
    }

    public ReadOnlyBoolean gt(ChangeableInt that) {
        return comparison(that, (a, b) -> a > b);
    }

    public ReadOnlyBoolean le(ChangeableInt that) {
        return comparison(that, (a, b) -> a <= b);
    }

    public ReadOnlyBoolean lt(ChangeableInt that) {
        return comparison(that, (a, b) -> a < b);
    }

    public ReadOnlyInt multiply(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a * b);
    }

    public ReadOnlyInt negate() {
        final var result = new ChangeableInt();
        final Listener listener = (sender, a) -> result.setValue(-a);
        addChangeListener(listener);
        return new ReadOnlyInt(result, () -> this.removeChangeListener(listener));
    }

    public void removeChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public ReadOnlyInt subtract(ChangeableInt that) {
        return binaryOp(that, (a, b) -> a - b);
    }

    private ReadOnlyInt binaryOp(ChangeableInt that, BinaryOperator operator) {
        final var result = new ChangeableInt();
        final Listener listenerThis = (sender, a) -> result.setValue(operator.calculate(a, that.value));
        final Listener listenerThat = (sender, b) -> result.setValue(operator.calculate(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return new ReadOnlyInt(result, () -> {
            this.removeChangeListener(listenerThis);
            that.removeChangeListener(listenerThat);
        });
    }

    private ReadOnlyBoolean comparison(ChangeableInt that, Comparison cmp) {
        final var result = new ChangeableBoolean();
        final Listener listenerThis = (sender, a) -> result.setValue(cmp.check(a, that.value));
        final Listener listenerThat = (sender, b) -> result.setValue(cmp.check(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return new ReadOnlyBoolean(result, () -> {
            this.removeChangeListener(listenerThis);
            that.removeChangeListener(listenerThat);
        });
    }

    private void fireChangeEvent() {
        final var lst = listeners.stream().toList();
        for (var listener : lst) {
            listener.onChange(this, value);
        }
    }

    private interface BinaryOperator {
        int calculate(int a, int b);
    }

    private interface Comparison {
        boolean check(int a, int b);
    }

    public interface Listener extends EventListener {
        void onChange(ChangeableInt sender, int value);
    }
}