package pixelj.util;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public final class ChangeableInt {

    private final Set<Listener> listeners = new HashSet<>();
    private int value;

    public ChangeableInt() {
        this(0);
    }

    public ChangeableInt(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int newValue) {
        if (newValue != value) {
            value = newValue;
            fireChangeEvent();
        }
    }

    /**
     * @param listener ChangeableInt.Listener
     */
    public void addChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * @param listener ChangeableList.Listener
     */
    public void removeChangeListener(final Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * @param that LHS
     * @return this + that
     */
    public ReadOnlyInt add(final ChangeableInt that) {
        return binaryOp(that, Integer::sum);
    }

    /**
     * @param that LHS
     * @return this - that
     */
    public ReadOnlyInt subtract(final ChangeableInt that) {
        return binaryOp(that, (a, b) -> a - b);
    }

    /**
     * @param that LHS
     * @return this * that
     */
    public ReadOnlyInt multiply(final ChangeableInt that) {
        return binaryOp(that, (a, b) -> a * b);
    }

    /**
     * @param that LHS
     * @return this / that
     */
    public ReadOnlyInt divide(final ChangeableInt that) {
        return binaryOp(that, (a, b) -> a / b);
    }

    /**
     * @param that LHS
     * @return this >= that
     */
    public ReadOnlyBoolean ge(final ChangeableInt that) {
        return comparison(that, (a, b) -> a >= b);
    }

    /**
     * @param that LHS
     * @return this > that
     */
    public ReadOnlyBoolean ge(final int that) {
        return comparison(that, (a, b) -> a >= b);
    }

    /**
     * @param that LHS
     * @return this > that
     */
    public ReadOnlyBoolean gt(final ChangeableInt that) {
        return comparison(that, (a, b) -> a > b);
    }

    /**
     * @param that LHS
     * @return this > that
     */
    public ReadOnlyBoolean gt(final int that) {
        return comparison(that, (a, b) -> a > b);
    }

    /**
     * @param that LHS
     * @return this <= that
     */
    public ReadOnlyBoolean le(final ChangeableInt that) {
        return comparison(that, (a, b) -> a <= b);
    }

    /**
     * @param that LHS
     * @return this <= that
     */
    public ReadOnlyBoolean le(final int that) {
        return comparison(that, (a, b) -> a <= b);
    }

    /**
     * @param that LHS
     * @return this < that
     */
    public ReadOnlyBoolean lt(final ChangeableInt that) {
        return comparison(that, (a, b) -> a < b);
    }

    /**
     * @param that LHS
     * @return this < that
     */
    public ReadOnlyBoolean lt(final int that) {
        return comparison(that, (a, b) -> a < b);
    }

    /**
     * @return !this
     */
    public ReadOnlyInt negate() {
        final var result = new ChangeableInt();
        final Listener listener = a -> result.setValue(-a);
        addChangeListener(listener);
        return new ReadOnlyInt(result, () -> this.removeChangeListener(listener));
    }

    /**
     * @param predicate
     * @return If the predicate returns true
     */
    public ReadOnlyBoolean test(final Predicate<Integer> predicate) {
        final var result = new ChangeableBoolean();
        final Listener listener = a -> result.setValue(predicate.test(a));
        addChangeListener(listener);
        return new ReadOnlyBoolean(result, () -> this.removeChangeListener(listener));
    }

    private ReadOnlyInt binaryOp(final ChangeableInt that, final BinaryOperator operator) {
        final var result = new ChangeableInt();
        final Listener listenerThis = a -> result.setValue(operator.calculate(a, that.value));
        final Listener listenerThat = b -> result.setValue(operator.calculate(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return new ReadOnlyInt(result, () -> {
            this.removeChangeListener(listenerThis);
            that.removeChangeListener(listenerThat);
        });
    }

    private ReadOnlyBoolean comparison(final ChangeableInt that, final Comparison cmp) {
        final var result = new ChangeableBoolean();
        final Listener listenerThis = a -> result.setValue(cmp.check(a, that.value));
        final Listener listenerThat = b -> result.setValue(cmp.check(value, b));
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return new ReadOnlyBoolean(result, () -> {
            this.removeChangeListener(listenerThis);
            that.removeChangeListener(listenerThat);
        });
    }

    private ReadOnlyBoolean comparison(final int that, final Comparison cmp) {
        final var result = new ChangeableBoolean();
        final Listener listenerThis = a -> result.setValue(cmp.check(a, that));
        this.addChangeListener(listenerThis);
        return new ReadOnlyBoolean(result, () -> this.removeChangeListener(listenerThis));
    }

    private void fireChangeEvent() {
        final var lst = listeners.stream().toList();
        for (var listener : lst) {
            listener.onChange(value);
        }
    }

    private interface BinaryOperator {
        int calculate(int a, int b);
    }

    private interface Comparison {
        boolean check(int a, int b);
    }

    public interface Listener extends EventListener {
        /**
         * @param value  The new value
         */
        void onChange(int value);
    }
}
