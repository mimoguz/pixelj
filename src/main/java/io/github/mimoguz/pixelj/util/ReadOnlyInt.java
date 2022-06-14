package io.github.mimoguz.pixelj.util;

public class ReadOnlyInt implements Detachable {
    private final Runnable cleaner;
    private final ChangeableInt delegate;

    public ReadOnlyInt(ChangeableInt delegate, Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyInt(ChangeableInt delegate) {
        this(delegate, null);
    }

    public ReadOnlyInt add(ReadOnlyInt that) {
        return delegate.add(that.delegate);
    }

    public ReadOnlyInt add(ChangeableInt that) {
        return delegate.add(that);
    }

    public void addChangeListener(ChangeableInt.Listener listener) {
        delegate.addChangeListener(listener);
    }

    @Override
    public void detach() {
        if (cleaner != null) {
            cleaner.run();
        }
    }

    public ReadOnlyInt divide(ReadOnlyInt that) {
        return delegate.divide(that.delegate);
    }

    public ReadOnlyInt divide(ChangeableInt that) {
        return delegate.divide(that);
    }

    public ReadOnlyBoolean ge(ChangeableInt that) {
        return delegate.ge(that);
    }

    public ReadOnlyBoolean ge(ReadOnlyInt that) {
        return delegate.ge(that.delegate);
    }

    public int getValue() {
        return delegate.getValue();
    }

    public ReadOnlyBoolean greaterThan(ChangeableInt that) {
        return delegate.gt(that);
    }

    public ReadOnlyBoolean greaterThan(ReadOnlyInt that) {
        return delegate.gt(that.delegate);
    }

    public ReadOnlyBoolean le(ChangeableInt that) {
        return delegate.le(that);
    }

    public ReadOnlyBoolean le(ReadOnlyInt that) {
        return delegate.le(that.delegate);
    }

    public ReadOnlyBoolean lt(ChangeableInt that) {
        return delegate.lt(that);
    }

    public ReadOnlyBoolean lt(ReadOnlyInt that) {
        return delegate.lt(that.delegate);
    }

    public ReadOnlyInt multiply(ReadOnlyInt that) {
        return delegate.multiply(that.delegate);
    }

    public ReadOnlyInt multiply(ChangeableInt that) {
        return delegate.multiply(that);
    }

    public void removeChangeListener(ChangeableInt.Listener listener) {
        delegate.removeChangeListener(listener);
    }

    public ReadOnlyInt subtract(ReadOnlyInt that) {
        return delegate.subtract(that.delegate);
    }

    public ReadOnlyInt subtract(ChangeableInt that) {
        return delegate.subtract(that);
    }
}
